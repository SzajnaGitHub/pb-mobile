package com.espresso.pbmobile.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.espresso.data.RetrofitClientInstance
import com.espresso.data.models.profile.UserProfile
import com.espresso.data.models.profile.UserRegisterInfo
import com.espresso.pbmobile.R
import com.espresso.pbmobile.main.MainActivity
import com.espresso.data.store.Store
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class AuthActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var store: Store
    private val disposables = CompositeDisposable()
    private val auth = FirebaseAuth.getInstance()
    private val service = RetrofitClientInstance.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        store = Store(this)
        googleSignInClient = GoogleSignIn.getClient(
            this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
        signIn()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                if(store.userId != Store.LONG_DEFAULT_VALUE)
                registerUser(UserRegisterInfo(uid = UUID.randomUUID().toString()))
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) {
            auth.currentUser?.let {
                registerUser(UserRegisterInfo(it.uid, it.email ?: "", it.displayName ?: ""))
                println("TEKST USER REGISTER INFO ${it.uid} ${it.email} ${it.displayName}")
            }
        }
    }

    private fun registerUser(userInfo: UserRegisterInfo) {
        service.register(userInfo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { user ->
                println("TEKST USER $user")
                store.userId = user.id
                store.userType = UserProfile.resolveType(user.userType) ?: Store.STRING_DEFAULT_VALUE
                goToNextScreen()
            }
            .let(disposables::add)
    }

    private fun goToNextScreen() {
        startActivity(MainActivity.createIntent(this).apply {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        fun createIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }
}
