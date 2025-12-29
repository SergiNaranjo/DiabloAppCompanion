package API

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.d3grimoire.R
import com.example.d3grimoire.information.ClassInformationActivity

object DiabloImageUrl {

    fun item(context: Context, icon: String, size: String = "large"): String =
        context.getString(R.string.item_icon_url,
            context.getString(R.string.icons_base_url),
            size, icon.removeSuffix(".png"));

    fun skill(context: Context, icon: String, size: Int = 64): String =
        context.getString(R.string.skill_icon_url,
            context.getString(R.string.icons_base_url),
            size, icon.removeSuffix(".png"));

    fun classPortrait(activity: AppCompatActivity, slug: String, gender: ClassInformationActivity.Gender): String {
        return when (gender) {
            ClassInformationActivity.Gender.MALE -> {
                activity.getString(R.string.male_portrait,
                    activity.getString(R.string.portrait_base_url), slug)
            }

            ClassInformationActivity.Gender.FEMALE -> {
                activity.getString(R.string.female_portrait,
                    activity.getString(R.string.portrait_base_url), slug)
            }
        }

    }

    fun classGif(activity: AppCompatActivity, slug: String): String {
        return when (slug) {
            activity.getString(R.string.barbarian_slug) ->
                activity.getString(R.string.class_gif_prefix,
                    activity.getString(R.string.class_gif_suffix_barbarian)
                );
            activity.getString(R.string.crusader_slug) ->
                activity.getString(R.string.class_gif_prefix,
                    activity.getString(R.string.class_gif_suffix_crusader)
                );
            activity.getString(R.string.demon_hunter_slug) ->
                activity.getString(R.string.class_gif_prefix,
                    activity.getString(R.string.class_gif_suffix_demon_hunter)
                );
            activity.getString(R.string.monk_slug) ->
                activity.getString(R.string.class_gif_prefix,
                    activity.getString(R.string.class_gif_suffix_monk)
                );
            activity.getString(R.string.necromancer_slug) ->
                activity.getString(R.string.class_gif_prefix,
                    activity.getString(R.string.class_gif_suffix_necromancer)
                );
            activity.getString(R.string.witch_doctor_slug) ->
                activity.getString(R.string.class_gif_prefix,
                    activity.getString(R.string.class_gif_suffix_witch_doctor)
                );
            activity.getString(R.string.wizard_slug) ->
                activity.getString(R.string.class_gif_prefix,
                    activity.getString(R.string.class_gif_suffix_wizard)
                );
            //Barbarian is taken as default
            else ->
                activity.getString(R.string.class_gif_prefix,
                    activity.getString(R.string.class_gif_suffix_barbarian)
                );
        }
    }
}