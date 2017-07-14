package codextreme.jimmyneutron.tradbooking

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import codextreme.jimmyneutron.Common
import codextreme.jimmyneutron.R


public class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.fragment_profile, container, false)
        val userprofile_name = view?.findViewById(R.id.userprofile_name) as TextView
        userprofile_name.text = Common.mUsername
        return view
    }
}
