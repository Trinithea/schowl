package cz.cizlmazna.schowl.ui.subjects


import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Subject



@BindingAdapter("subjectNamed")
fun Button.setNameOfSubject(item: Subject?){
    item?.let{
        text = item.name
       /**/ if(MainActivity.getDarkMode())
            setTextColor(ContextCompat.getColor(context!!, R.color.white))
        else
            setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
    }
}

@BindingAdapter("imageEditResource")
fun ImageButton.setEditResourceImage(item: Subject?){
    item?.let{
      /**/  if(MainActivity.getDarkMode())
            setImageResource(R.drawable.ic_edit_yellow)
        else
            setImageResource(R.drawable.ic_edit_blue)
    }

}

@BindingAdapter("imageRemoveResource")
fun ImageButton.setRemoveResourceImage(item: Subject?){
    item?.let{
      /**/  if(MainActivity.getDarkMode())
            setImageResource(R.drawable.ic_remove_yellow)
        else
            setImageResource(R.drawable.ic_remove_blue)
    }

}

@BindingAdapter("imageTestResource")
fun ImageButton.setTestResourceImage(item: Subject?){
    item?.let{
       /**/ if(MainActivity.getDarkMode())
            setImageResource(R.drawable.ic_test_yellow)
        else
            setImageResource(R.drawable.ic_test_blue)/**/
    }

}


