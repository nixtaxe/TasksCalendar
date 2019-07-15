package zabortceva.eventscalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import zabortceva.eventscalendar.R;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;

    SignInButton signInButton;
    FloatingActionButton signOutButton;
    FloatingActionButton openCalendarButton;
    FloatingActionButton shareCalendarButton;
    TextView welcomeText;
    TextView nameView;
    TextView emailView;
    ImageView photoView;

    RotateLoading rotateLoading;

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        rotateLoading = findViewById(R.id.rotateloading);
        switchLoading();

        welcomeText = findViewById(R.id.welcome_text);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        nameView = findViewById(R.id.account_name);
        emailView = findViewById(R.id.account_email);
        photoView = findViewById(R.id.account_photo);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                updateUI(user);
            }
        };

        signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        openCalendarButton = findViewById(R.id.open_calendar_button);
        openCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CalendarActivity.class));
                finish();
            }
        });

        shareCalendarButton = findViewById(R.id.share_menu_button);
        shareCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(LoginActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, ShareActivity.class));
                finish();
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null)
            refreshToken();

        switchLoading();
    }

    private void refreshToken() {
        mAuth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                Log.d("Token", "Refreshed");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void switchLoading() {
        if (rotateLoading.isStart()) {
            rotateLoading.stop();
        } else {
            rotateLoading.start();
        }
    }

    public void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        switchLoading();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            try {
                // Google Sign In was successful, authenticate with Firebase
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null)
                    firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Sign in failure", Toast.LENGTH_SHORT).show();
                        }

                        switchLoading();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        switchLoading();
        if (user != null) {
            nameView.setText(user.getDisplayName());
            emailView.setText(user.getEmail());
            Picasso.get().load(user.getPhotoUrl()).into(photoView);

            signInButton.setVisibility(View.GONE);
            welcomeText.setVisibility(View.GONE);

            photoView.setVisibility(View.VISIBLE);
            nameView.setVisibility(View.VISIBLE);
            emailView.setVisibility(View.VISIBLE);
            signOutButton.show();
            openCalendarButton.show();
            shareCalendarButton.show();
        } else {
            photoView.setVisibility(View.GONE);
            nameView.setVisibility(View.GONE);
            emailView.setVisibility(View.GONE);
            signOutButton.hide();
            openCalendarButton.hide();
            shareCalendarButton.hide();

            signInButton.setVisibility(View.VISIBLE);
            welcomeText.setVisibility(View.VISIBLE);
        }
        switchLoading();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            finish();
        }
    }
}

