package com.imperium.accessabilityapp.Services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Browser
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.imperium.accessabilityapp.Uils.URL_LIST
import com.imperium.accessabilityapp.Uils.checkIsTimeBetween
import com.imperium.accessabilityapp.presentation.Activities.AccessDeniedActivity
import com.imperium.accessabilityapp.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UrlInterceptorService : AccessibilityService() {
    @Inject
    lateinit var repository: MainRepository
    private val previousUrlDetections = HashMap<String, Long>()
    override fun onServiceConnected() {
        val info = serviceInfo
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        info.packageNames = packageNames()
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
        //throttling of accessibility event notification
        info.notificationTimeout = 300
        //support ids interception
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        this.serviceInfo = info
    }

    private fun captureUrl(info: AccessibilityNodeInfo, config: SupportedBrowserConfig): String? {
        val nodes = info.findAccessibilityNodeInfosByViewId(config.addressBarId)
        if (nodes == null || nodes.size <= 0) {
            return null
        }
        val addressBarNodeInfo = nodes[0]
        var url: String? = null
        if (addressBarNodeInfo.text != null) {
            url = addressBarNodeInfo.text.toString()
        }
        addressBarNodeInfo.recycle()
        return url
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val parentNodeInfo = event.source ?: return
        val packageName = event.packageName.toString()
        var browserConfig: SupportedBrowserConfig? = null
        for (supportedConfig in supportedBrowsers) {
            if (supportedConfig.packageName == packageName) {
                browserConfig = supportedConfig
            }
        }
        //this is not supported browser, so exit
        if (browserConfig == null) {
            return
        }
        val capturedUrl = captureUrl(parentNodeInfo, browserConfig)
        parentNodeInfo.recycle()

        //we can't find a url. Browser either was updated or opened page without url text field
        if (capturedUrl == null) {
            return
        }
        val eventTime = event.eventTime
        val detectionId = "$packageName, and url $capturedUrl"
        val lastRecordedTime =
            if (previousUrlDetections.containsKey(detectionId)) previousUrlDetections[detectionId]!! else 0
        //some kind of redirect throttling
        if (eventTime - lastRecordedTime > 4000) {
            previousUrlDetections[detectionId] = eventTime
            analyzeCapturedUrl(capturedUrl, browserConfig.packageName)
        }
    }

    private fun analyzeCapturedUrl(capturedUrl: String, browserPackage: String) {

        if(repository.isEventCreated()){
            val switchState=repository.getSwitchState()
            val startTime=repository.getStartTime24()
            val endTime=repository.getEndTime24()
            if(startTime!=null && endTime!=null) {
                val isInBlockedState = checkIsTimeBetween(startTime = startTime, endTime = endTime)
                if (switchState && isInBlockedState) {//blacklist
                    if(capturedUrl!="Search or type web address" && URL_LIST.contains(capturedUrl.toLowerCase())){
                        openAccessDenied()
                    }
                } else if(!switchState && isInBlockedState) {//whitelist
                    var filterdUrl=""
                    if(capturedUrl.contains("mobile.")){
                        filterdUrl=capturedUrl.replace("mobile.","")
                    }else if(capturedUrl.contains("m.")) {
                        filterdUrl = capturedUrl.replace("m.", "")
                    }else{
                        filterdUrl=capturedUrl
                    }
                    if(capturedUrl!="Search or type web address" && capturedUrl !in URL_LIST){
                        openAccessDenied()
                    }
                    /*if(capturedUrl!="Search or type web address" && !URL_LIST.contains(filterdUrl.toLowerCase())){
                        print("contains")
                        openAccessDenied()
                    }*/



                }
            }
        }

    }

    private fun openAccessDenied() {
        val intent = Intent(this, AccessDeniedActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        applicationContext.startActivity(intent)
    }


    override fun onInterrupt() {}
    private class SupportedBrowserConfig(var packageName: String, var addressBarId: String)
    companion object {
        private fun packageNames(): Array<String> {
            val packageNames: MutableList<String> = ArrayList()
            for (config in supportedBrowsers) {
                packageNames.add(config.packageName)
            }
            return packageNames.toTypedArray()
        }

        /** @return a list of supported browser configs
         * This list could be instead obtained from remote server to support future browser updates without updating an app
         */
        private val supportedBrowsers: List<SupportedBrowserConfig>
            private get() {
                val browsers: MutableList<SupportedBrowserConfig> = ArrayList()
                browsers.add(
                    SupportedBrowserConfig(
                        "com.android.chrome",
                        "com.android.chrome:id/url_bar"
                    )
                )
                browsers.add(
                    SupportedBrowserConfig(
                        "org.mozilla.firefox",
                        "org.mozilla.firefox:id/url_bar_title"
                    )
                )
                return browsers
            }
    }
}