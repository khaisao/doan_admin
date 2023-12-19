
package com.khaipv.attendance.testFaceReco

import android.util.Log
import com.khaipv.attendance.ui.teacher.faceReco.FaceRecoFaceNetFragment

// Logs message using log_textview present in activity_main.xml
class Logger {

    companion object {

        fun log( message : String ) {
            FaceRecoFaceNetFragment.setMessage(FaceRecoFaceNetFragment.logTextView.text.toString() + "\n" + ">> $message")
            Log.d("sagawgawgawg", "log: $message")
            // To scroll to the last message
            // See this SO answer -> https://stackoverflow.com/a/37806544/10878733
            while ( FaceRecoFaceNetFragment.logTextView.canScrollVertically(1) ) {
                FaceRecoFaceNetFragment.logTextView.scrollBy(0, 10)
            }
        }

    }

}