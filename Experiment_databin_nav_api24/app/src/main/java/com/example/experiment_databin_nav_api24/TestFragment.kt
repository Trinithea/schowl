package com.example.experiment_databin_nav_api24


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.experiment_databin_nav_api24.databinding.FragmentCategoriesBinding
import com.example.experiment_databin_nav_api24.databinding.FragmentTestBinding

class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTestBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_test,container,false)
        (activity as AppCompatActivity).supportActionBar?.title = "TEST"
        binding.TxtSolution.text="Netflix officially announced on Tuesday the show Friends, one of the most popular shows on its platform, would no longer be available for streaming come 2020.\n" +
                "\n" +
                "\"The One Where We Have To Say Goodbye,\" the streaming giant tweeted, a subtle nod to how Friends famously titled episodes.\n" +
                "\n" +
                "\"We're sorry to see Friends go,\" the company said, right before announcing the show's new home.\n" +
                "\n" +
                "Speculation had been brewing for a while that Netflix would be relinquishing the beloved title, which the company reportedly paid \$100 million to keep last year — The show will instead be featured on WarnerMedia's upcoming streaming service, HBO Max.\n" +
                "\n" +
                "Fans of the series, again, didn't take the news so well.\n" +
                "\n" +
                "There were countless appropriately timed Friends GIFs used.\n" +
                "\n" +
                "And a ton of jokes, presumably from Netflix subscribers, about how this news may lead people to cancel their service.\n" +
                "\n" +
                "Netflix also parted ways with The Office, another heavily watched show that will be leaving the platform in 2021— a double dose of bad news for fans of both series.\n" +
                "\n" +
                "There were a few folks who criticized the company for choosing not to keep shows like One Day at a Time on its service, especially after shelling out so much money to keep Friends.\n" +
                "\n" +
                "    y'all just spent all that fucking money to save it instead of renewing One day at a time, lmao now you lost Friends and one day at a time has a new network bitch https://t.co/AgeiGpAAAY\n" +
                "    Micah | ST ruined me\n" +
                "    @MagicalMicah\n" +
                "\n" +
                "    y'all just spent all that fucking money to save it instead of renewing One day at a time, lmao now you lost Friends and one day at a time has a new network bitch https://t.co/AgeiGpAAAY\n" +
                "    05:25 PM - 09 Jul 2019\n" +
                "    Reply Retweet Favorite\n" +
                "\n" +
                "The sitcom will be available on Netflix for the remainder of the year. It is unclear exactly when it will be removed from the platform. Netflix did not immediately respond to BuzzFeed News.\n" +
                "Topics In This Article\n" +
                "\n" +
                "    Netflix\n" +
                "\n" +
                "    Picture of Michael Blackmon\n" +
                "\n" +
                "    Michael Blackmon is an entertainment reporter for BuzzFeed News and is based in New York.\n" +
                "\n" +
                "    Contact Michael Blackmon at michael.blackmon@buzzfeed.com.\n" +
                "\n" +
                "    Got a confidential tip? Submit it here.\n" +
                "\n"
        return binding.root

    }


}
