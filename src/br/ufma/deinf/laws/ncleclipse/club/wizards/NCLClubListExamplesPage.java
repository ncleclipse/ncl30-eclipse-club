/*******************************************************************************
 * This file is part of the NCL authoring environment - NCL Eclipse.
 *
 * Copyright (C) 2007-2012, LAWS/UFMA.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2 for
 * more details. You should have received a copy of the GNU General Public 
 * License version 2 along with this program; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
 * 02110-1301, USA.
 *
 * For further information contact:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 ******************************************************************************/
package br.ufma.deinf.laws.ncleclipse.club.wizards;

import br.ufma.deinf.laws.ncleclipse.club.ClubNCLMessages;
import br.ufma.deinf.laws.ncleclipse.club.rss.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.xml.sax.SAXException;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NCLClubListExamplesPage extends WizardPage {
	private List listExamples;

	private Browser selectedDescription;

	private ISelection selection;
	private RSSReader rssReader;
	private Vector<RSSItem> items;
	private RSSItem selectedItem;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NCLClubListExamplesPage(ISelection selection) {
		super("wizardPage");
		setTitle(ClubNCLMessages.getInstance().getString("WizardPage.title"));
		setDescription(ClubNCLMessages.getInstance().getString(
				"WizardPage.description"));
		this.selection = selection;
	}

	public RSSItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(RSSItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	public RSSReader getRssReader() {
		return rssReader;
	}

	public void setRssReader(RSSReader rssReader) {
		this.rssReader = rssReader;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */

	public void createControl(Composite parent) {
		parent.setBounds(0, 0, 800, 500); // resize the window
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.makeColumnsEqualWidth = true;
		layout.numColumns = 2;

		GridData data = new GridData(GridData.FILL_BOTH);

		listExamples = new List(container, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER);
		listExamples.setLayoutData(data);

		data = new GridData(GridData.FILL_BOTH);

		selectedDescription = new Browser(container, SWT.NONE | SWT.BORDER);
		selectedDescription.setLayoutData(data);
		selectedDescription.setBounds(0, 0, 500, 500);

		// get RSS from Web
		URL url;
		try {
			url = new URL(ClubNCLMessages.getInstance().getString(
					"RSS_LOCATION"));
			rssReader = new RSSReader(url);
			rssReader.parse();
			items = rssReader.getRssRoot().getAllItems();

			// build the list from RSS
			for (int i = 0; i < items.size(); i++) {
				listExamples.add(items.get(i).getTitle());
			}

			selectedDescription.setText(ClubNCLMessages.getInstance()
					.getString("SelectedDescription.empty"));
			selectedDescription.setSize(200, 200);

			listExamples.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int sel = listExamples.getSelectionIndex();
					if (sel == -1)
						return;
					String descr = "";
					descr = "<img src='"
							+ items.get(sel).getImageUrl().toString() + "'/>";

					descr += items.get(sel).getDescription();

					selectedDescription.setText(descr);

					selectedItem = items.get(sel);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					int selectedItem = listExamples.getSelectionIndex();
					if (selectedItem == -1)
						return;
					selectedDescription.setText(items.get(selectedItem)
							.getDescription());
				}
			});

		} catch (MalformedURLException e) {
			selectedDescription.setText(ClubNCLMessages.getInstance()
					.getString("SelectedDescription.ConnectionError"));
			e.printStackTrace();
		} catch (SAXException e) {
			selectedDescription.setText(ClubNCLMessages.getInstance()
					.getString("SelectedDescription.ConnectionError"));
			e.printStackTrace();
		} catch (IOException e) {
			selectedDescription
					.setText("NCL Club can't be contacted. Please, check your internet connection. If you are sure that your connection is Ok send an e-mail to ncleclipse@laws.deinf.ufma.br");
			e.printStackTrace();
		}

		container.setBounds(0, 0, 1200, 800);
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
			}
		}
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				// containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {

	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
}
