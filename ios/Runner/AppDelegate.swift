import UIKit
import Flutter
import video_editor_sdk
import ImglyKit

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    GeneratedPluginRegistrant.register(with: self)

      try? VESDK.replaceClass(VideoEditViewController.self, with: CustomVideoEditor.self)

      FlutterVESDK.configureWithBuilder = { builder in
          builder.configureVideoEditViewController { options in
              options.applyButtonConfigurationClosure = { button in
                  button.isHidden = true
              }

              options.discardButtonConfigurationClosure = { button in
                  button.isHidden = true
              }
          }
      }

    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}
