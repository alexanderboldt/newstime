package com.alex.newstimes.feature.article

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alex.newstime.R
import com.alex.newstime.feature.article.ArticleController
import com.alex.newstime.repository.article.Article
import com.bluelinelabs.conductor.RouterTransaction
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleControllerTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(TestActivity::class.java)

    private lateinit var article: Article

    // ----------------------------------------------------------------------------

    @Before
    fun before() {
        rule.runOnUiThread {
            article = Article().apply {
                id = 2342
                title = "Test-DbArticle"
                urlToImage = "www.url.image"
                content = "This is a test-content"
            }
            rule.activity.router.setRoot(RouterTransaction.with(ArticleController.create(article)))
        }
    }

    @Test
    fun it_should_display_the_article() {
        // caution: instead of Thread.sleep the IdlingResource-functionality is recommended
        Thread.sleep(1000)

        onView(withId(R.id.textView_title)).check(matches(withText(article.title)))
        onView(withId(R.id.textView_content)).check(matches(withText(article.content)))
    }
}