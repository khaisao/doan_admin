
package com.example.baseproject.testFaceReco

import android.util.Log
import com.example.baseproject.ui.teacher.faceReco.FaceRecoFragment

// Logs message using log_textview present in activity_main.xml
class Logger {

    companion object {

        fun log( message : String ) {
            FaceRecoFragment.setMessage(FaceRecoFragment.logTextView.text.toString() + "\n" + ">> $message")
            Log.d("sagawgawgawg", "log: $message")
            // To scroll to the last message
            // See this SO answer -> https://stackoverflow.com/a/37806544/10878733
            while ( FaceRecoFragment.logTextView.canScrollVertically(1) ) {
                FaceRecoFragment.logTextView.scrollBy(0, 10)
            }
        }

    }

}