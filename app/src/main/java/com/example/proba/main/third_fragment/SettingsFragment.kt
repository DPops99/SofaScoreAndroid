package com.example.proba.main.third_fragment

import android.content.Context
import android.content.Intent
import android.os.*
import android.os.Process
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.proba.R
import com.example.proba.aboutMe.AboutMeActivity
import com.example.proba.databinding.SettingsLayoutBinding
import com.example.proba.helper.ConverterHelper
import com.example.proba.helper.LanguageHelper
import com.example.proba.main.MainActivity
import com.example.proba.main.view_model.ApiViewModel
import com.example.proba.room.viewmodel.RoomFactory
import com.example.proba.room.viewmodel.RoomViewModel

import java.util.*

class SettingsFragment : Fragment() {

    private var _binding : SettingsLayoutBinding? = null
    private val binding get()=_binding!!
    private lateinit var roomViewModel : RoomViewModel
    private lateinit var roomViewModelFactory : RoomFactory
    private val apiViewModel : ApiViewModel by activityViewModels()
    private lateinit var langArray : Array<String>
    private val METRIC = "METRIC"
    private val IMPERIAL = "IMPERIAL"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsLayoutBinding.inflate(inflater,container,false)
        val view = binding.root

        roomViewModelFactory = RoomFactory(requireContext())
        roomViewModel = ViewModelProvider(this,roomViewModelFactory).get(RoomViewModel::class.java)

        setView()
        setListeners()
        return view
    }


    fun setView(){
        langArray = requireContext().resources.getStringArray(R.array.available_languages)
        binding.settingsAuto.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, langArray))

        if (ConverterHelper.getPreferedMetric(requireContext()).getString(ConverterHelper.PREF_METRIC_CODE,ConverterHelper.DEFAULT_METRIC)==ConverterHelper.DEFAULT_METRIC)
            binding.settingsRadio.check(R.id.settings_metric)
        else
            binding.settingsRadio.check(R.id.settings_imperial)

    }

    fun setListeners(){
        binding.clearCitiesBtn.setOnClickListener {
            roomViewModel.deleteRecentCities()
        }
        binding.clearFavCitiesBtn.setOnClickListener {
            roomViewModel.deleteFavoriteCities()
        }
        binding.settingsAuto.setOnItemClickListener { parent, view, position, id ->
            setLocale(languageStringChanger(parent.getItemAtPosition(position) as String, langArray ), requireContext())}
        binding.settingsRadio.setOnCheckedChangeListener { group, checkedId -> if(checkedId==R.id.settings_metric) ConverterHelper.setPreferedMetric(requireContext(), ConverterHelper.DEFAULT_METRIC)
                                                            else ConverterHelper.setPreferedMetric(requireContext(), IMPERIAL)}

        binding.moreInfoBtn.setOnClickListener {
            startActivity(Intent(context, AboutMeActivity::class.java))
        }

    }

    fun languageStringChanger(lang : String, langArray : Array<String>) = if (lang==langArray[0]) "en" else "hr"


    fun setLocale(localeName : String, context: Context){
        val locale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            val localList = LocaleList(locale)
            LocaleList.setDefault(localList)
            conf.setLocales(localList)
        }
        else
            conf.locale = locale

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            context?.createConfigurationContext(conf)
        else
            res.updateConfiguration(conf,dm)

        var intent: Intent = Intent(context,MainActivity::class.java)
        intent.putExtra(getString(R.string.snackbar_bundle),if (localeName=="en") "hr" else "en")

        startActivity(intent)
        this.activity?.finish()


//        LanguageHelper.setPreferredLanguage(context, localeName)
//        restartApp()
    }

    private fun restartApp() {

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(activity, MainActivity::class.java)
            val cn = intent.component
            val mainIntent = Intent.makeRestartActivityTask(cn)
            startActivity(mainIntent)
            Process.killProcess(Process.myPid())
        }, 300)

    }
}