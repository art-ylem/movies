package ru.androidschool.intensiv.ui.profile

import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.DetailedMovieRoom
import ru.androidschool.intensiv.myObserve
import ru.androidschool.intensiv.room.AppDB
import ru.androidschool.intensiv.ui.watchlist.MoviePreviewItem

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var profileTabLayoutTitles: Array<String>
    private val cd = CompositeDisposable()
    private val db by lazy {
        Room.databaseBuilder(
            requireContext(),
            AppDB::class.java, movieDB
        ).build()
    }
    private val adapter by lazy { GroupAdapter<GroupieViewHolder>() }

    private var profilePageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Toast.makeText(
                requireContext(),
                "Selected position: $position",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get()
            .load(R.drawable.ic_avatar)
            .transform(CropCircleTransformation())
            .placeholder(R.drawable.ic_avatar)
            .into(avatar)

        profileTabLayoutTitles = resources.getStringArray(R.array.tab_titles)

        val profileAdapter = ProfileAdapter(
            this,
            profileTabLayoutTitles.size
        )
        doppelgangerViewPager.adapter = profileAdapter

        doppelgangerViewPager.registerOnPageChangeCallback(profilePageChangeCallback)

        TabLayoutMediator(tabLayout, doppelgangerViewPager) { tab, position ->

            // Выделение первой части заголовка таба
            // Название таба
            val title = profileTabLayoutTitles[position]
            // Раздеряем название на части. Первый элемент будет кол-во
            val parts = profileTabLayoutTitles[position].split(" ")
            val number = parts[0]
            val spannableStringTitle = SpannableString(title)
            spannableStringTitle.setSpan(RelativeSizeSpan(2f), 0, number.count(), 0)

            tab.text = spannableStringTitle
        }.attach()


        val dis = db.movies().getAll().myObserve()
            .subscribe({
                dataLoaded(it)
            }, {
                val err = it
            })

        cd.add(dis)

    }

    private fun dataLoaded(data: List<DetailedMovieRoom>?) {
        wish_list_recycler_view.adapter = adapter.apply {
            data?.map { roomItem ->
                MoviePreviewItem(
                    Pair(roomItem.posterPath, roomItem.id)
                ) {
                    //TODO go to detailed movie fragment by id
                }
            }?.toList()?.let { addAll(it) }
        }
    }

    override fun onStop() {
        super.onStop()
        cd.clear()
    }


    companion object {
        private const val movieDB = "movie_database"
    }
}
