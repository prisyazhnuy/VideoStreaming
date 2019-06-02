package com.prisyazhnuy.streaming.ui.screens.main.stream.jitsi

import android.os.Bundle
import android.view.View
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_jitsi.*
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL


class JitsiFragment : BaseFragment<JitsiVM>(),
View.OnClickListener{
    override val containerId = NO_ID
    override val layoutId = R.layout.fragment_jitsi
    override val viewModelClass = JitsiVM::class.java

    companion object {
        private val NAME = MiscellaneousUtils.getExtra("NAME", JitsiFragment::class.java)

        fun newInstance(streamName: String) = JitsiFragment().apply {
            arguments = Bundle().apply {
                putString(NAME, streamName)
            }
        }
    }

    override fun getScreenTitle() = NO_TITLE
    override fun getToolbarId() = NO_TOOLBAR
    override fun hasToolbar() = false
    override fun observeLiveData(viewModel: JitsiVM) = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState).also {
            initConnection()
            setClickListeners(btnJoin)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnJoin -> startStream()
        }
    }

    private fun initConnection() {
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://meet.jit.si"))
                .setWelcomePageEnabled(false)
                .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
    }

    private fun startStream() {
        val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(arguments?.getString(NAME))
                .build()
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        context?.let { ctx ->
            JitsiMeetActivity.launch(ctx, options)
        }
    }
}