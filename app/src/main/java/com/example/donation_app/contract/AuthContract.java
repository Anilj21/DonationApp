package com.example.donation_app.contract;

public interface AuthContract {

    interface View {
        void onLoginSuccess();
        void onLoginFailure(String message);

        void onRegisterSuccess();
        void onRegisterFailure(String message);
    }

    interface Presenter {
        void login(String email, String password);
        void register(String name, String email, String password, String role, boolean isVolunteer);
    }
}
