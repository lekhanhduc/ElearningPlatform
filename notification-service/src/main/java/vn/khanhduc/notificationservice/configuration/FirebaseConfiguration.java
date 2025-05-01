//package vn.khanhduc.notificationservice.configuration;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.messaging.FirebaseMessaging;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Configuration
//public class FirebaseConfiguration {
//
//    @Value("${firebase.service-account}")
//    private String serviceAccountPath;
//
//    @Bean
//    public FirebaseApp firebaseApp() throws IOException {
//        FileInputStream inputStream = new FileInputStream(serviceAccountPath);
//        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(inputStream))
//                .build();
//        return FirebaseApp.initializeApp(firebaseOptions);
//    }
//
//    @Bean
//    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
//        return FirebaseMessaging.getInstance(firebaseApp);
//    }
//
//}
