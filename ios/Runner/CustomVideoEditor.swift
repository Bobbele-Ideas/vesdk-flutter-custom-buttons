//
//  CustomVideoEditor.swift
//  Runner
//
//  Created by Leon Dudlik on 26.07.22.
//

import Foundation
import ImglyKit

class CustomVideoEditor: VideoEditViewController {
    private var backButton: UIButton?

    override func viewDidLoad() {
        super.viewDidLoad()

        addExportButton()
        addCancelButton()
    }

    /// Adds the export button to the editor's toolbar.
    func addExportButton() {
        guard let contentView = toolbarItem.titleView else { return }

        if let label = contentView as? UILabel {
            label.text = nil
        }

        let button = UIButton()
        button.setTitle("Export", for: .normal)
        contentView.addSubview(button)
        button.translatesAutoresizingMaskIntoConstraints = false

        var constraints = [NSLayoutConstraint]()
        constraints.append(button.centerXAnchor.constraint(equalTo: contentView.centerXAnchor))
        constraints.append(button.centerYAnchor.constraint(equalTo: contentView.centerYAnchor))
        constraints.append(button.heightAnchor.constraint(equalTo: contentView.heightAnchor, constant: -10))
        NSLayoutConstraint.activate(constraints)

        contentView.isUserInteractionEnabled = true
        button.addTarget(self, action: #selector(export(sender:)), for: .touchUpInside)
    }

    /// Adds the cancel button to the editor's toolbar.
    func addCancelButton() {
        let button = UIButton()
        button.setImage(UIImage(systemName: "chevron.backward"), for: .normal)
        button.tintColor = UIColor.white
        button.addTarget(self, action: #selector(cancel(sender:)), for: .touchUpInside)

        view.addSubview(button)
        view.isUserInteractionEnabled = true
        button.translatesAutoresizingMaskIntoConstraints = false

        var constraints = [NSLayoutConstraint]()
        constraints.append(button.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 50))
        constraints.append(button.leftAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leftAnchor, constant: 25))
        constraints.append(button.heightAnchor.constraint(equalToConstant: 50))
        constraints.append(button.widthAnchor.constraint(equalToConstant: 50))
        NSLayoutConstraint.activate(constraints)

        backButton = button
    }

    /// The action of the export button.
    @objc func export(sender _: UIButton) {
        renderHighResolutionVariant()
    }

    /// The action of the cancel button.
    @objc func cancel(sender _: UIButton) {
        IMGLY.analytics.logEvent(.discardChanges)
        notifySubscribers { $0.viewControllerDidCancel(self) }
    }

    override func willPresent(_ toolController: PhotoEditToolController) {
      super.willPresent(toolController)

      backButton?.isHidden = true
    }

    override func willDismiss(_ toolController: PhotoEditToolController) {
      super.willDismiss(toolController)

      backButton?.isHidden = false
    }
}
