package com.x200s.cleanassistant

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val prefs by lazy { getSharedPreferences("progress", MODE_PRIVATE) }
    data class Guide(val id: String, val title: String, val description: String, val steps: String, val action: String)

    private val guides = listOf(
        Guide("privacy", "系统广告与隐私", "检查系统隐私、广告个性化和个性化推荐相关选项。", "打开系统设置后，进入隐私或安全与隐私相关页面。查找“广告”“个性化广告”“个性化推荐”等选项；只关闭明确属于广告个性化的开关。", "privacy"),
        Guide("homescreen", "负一屏推荐", "检查智慧桌面、负一屏或智能助手中的资讯与服务推荐。", "在桌面向右滑进入负一屏/智慧桌面。打开页面设置，检查资讯、热点、服务推荐等开关。不同 OriginOS 版本的名称和入口可能不同。", "home"),
        Guide("search", "全局搜索热搜", "检查搜索页面的热搜榜、热点和内容推荐。", "打开手机全局搜索页面，进入搜索设置或页面管理。按需关闭热搜榜、热点推荐和个性化内容推荐。", "search"),
        Guide("store", "应用商店推荐", "检查应用商店中的个性化推荐、开屏营销和通知。", "打开 vivo 应用商店，进入“我的”或设置页面，检查消息通知、个性化推荐、活动推广等选项。只关闭不需要的营销项目。", "app"),
        Guide("browser", "浏览器资讯推荐", "检查浏览器首页的信息流、热点和个性化内容。", "打开浏览器设置，查看首页内容、资讯、热点、个性化推荐和通知管理。按需关闭资讯流或营销推送。", "browser"),
        Guide("quick", "快应用管理", "检查快应用入口及其通知、推荐内容。", "在系统设置或应用管理中查找“快应用”相关管理入口。确认不需要后，可关闭其通知或推荐入口；不要随意清除系统组件数据。", "app"),
        Guide("notifications", "营销通知管理", "逐个检查应用通知，关闭不需要的营销通知类别。", "进入设置中的通知管理/应用通知。选择具体应用，关闭营销、活动、推荐等通知类别；保留验证码、订单和安全提醒。", "notifications")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()
    }

    private fun render() {
        val scroll = ScrollView(this)
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(28))
            setBackgroundColor(0xFFF5F7F6.toInt())
        }
        val title = TextView(this).apply {
            text = "X200s 清爽助手"
            textSize = 26f
            setTextColor(0xFF173B2E.toInt())
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        page.addView(title)
        page.addView(text("vivo X200s · OriginOS 6 设置引导版", 14f, 0xFF607168))
        page.addView(space(16))
        val summary = TextView(this).apply {
            text = "已完成 ${guides.count { prefs.getBoolean(it.id, false) }} / ${guides.size} 项\n本应用只提供本地引导，不会自动修改系统设置。"
            textSize = 17f
            setTextColor(0xFF173B2E.toInt())
            setPadding(dp(16), dp(16), dp(16), dp(16))
            setBackgroundColor(0xFFE2F0E9.toInt())
        }
        page.addView(summary)
        page.addView(space(12))
        guides.forEach { guide ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(16), dp(14), dp(16), dp(14))
                setBackgroundColor(0xFFFFFFFF.toInt())
            }
            card.addView(text(guide.title, 18f, 0xFF173B2E, true))
            card.addView(space(5))
            card.addView(text(guide.description, 14f, 0xFF58665F))
            card.addView(space(8))
            card.addView(text("操作建议：${guide.steps}", 14f, 0xFF303A35))
            card.addView(space(10))
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }
            val open = Button(this).apply {
                text = "打开相关设置"
                setOnClickListener { openSettings(guide.action) }
            }
            row.addView(open, LinearLayout.LayoutParams(0, dp(48), 1f))
            val check = CheckBox(this).apply {
                text = "已检查"
                isChecked = prefs.getBoolean(guide.id, false)
                setOnCheckedChangeListener { _, checked ->
                    prefs.edit().putBoolean(guide.id, checked).apply()
                    summary.text = "已完成 ${guides.count { prefs.getBoolean(it.id, false) }} / ${guides.size} 项\n本应用只提供本地引导，不会自动修改系统设置。"
                }
            }
            row.addView(check)
            card.addView(row)
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.bottomMargin = dp(10)
            page.addView(card, lp)
        }
        page.addView(space(8))
        page.addView(text("安全提示：不同 OriginOS 版本的菜单名称可能不同。关闭前请确认选项含义；不要停用电话、短信、系统桌面、系统更新或安全相关组件。完成勾选只代表你已检查，不代表广告已被全部屏蔽。", 13f, 0xFF7A5140))
        page.addView(space(12))
        page.addView(Button(this).apply {
            text = "清除本机完成记录"
            setOnClickListener {
                prefs.edit().clear().apply()
                Toast.makeText(this@MainActivity, "本地记录已清除", Toast.LENGTH_SHORT).show()
                render()
            }
        })
        scroll.addView(page)
        setContentView(scroll)
    }

    private fun openSettings(kind: String) {
        val intent = when (kind) {
            "privacy" -> Intent(Settings.ACTION_PRIVACY_SETTINGS)
            "notifications" -> Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            else -> Intent(Settings.ACTION_SETTINGS)
        }
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        } catch (_: Exception) {
            Toast.makeText(this, "无法打开该设置，请从系统设置手动查找", Toast.LENGTH_LONG).show()
        }
    }

    private fun text(value: String, size: Float, color: Int, bold: Boolean = false) =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

    private fun space(height: Int) = View(this).apply {
        layoutParams = LinearLayout.LayoutParams(1, dp(height))
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
