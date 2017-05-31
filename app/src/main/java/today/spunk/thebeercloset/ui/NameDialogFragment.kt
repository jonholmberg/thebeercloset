package today.spunk.thebeercloset.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.find
import today.spunk.thebeercloset.R

/**
 * Created by jonholmberg on 31/05/2017.
 */
class NameDialogFragment(val callback : ((String) -> Unit)? = null) : DialogFragment() {

    var text : String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.dialog_name, null)
        val dialog = AlertDialog.Builder(activity).setTitle(R.string.nameDialog)
                .setView(view)
                .setNeutralButton(R.string.ok, { _, _ ->
                    text = view.find<EditText>(R.id.nameView).text.toString()
                }).create()
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        callback?.invoke(text)
    }
}