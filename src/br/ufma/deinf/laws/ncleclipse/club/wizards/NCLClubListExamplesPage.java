package br.ufma.deinf.laws.ncleclipse.club.wizards;

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

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NCLClubListExamplesPage(ISelection selection) {
		super("wizardPage");
		setTitle("NCL Eclipse - NCL Examples from NCL Club");
		setDescription("This wizard creates a new file with *.ncl extension based on examples avalaible at http://clube.ncl.org.br.");
		this.selection = selection;
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
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.makeColumnsEqualWidth = true;
		layout.numColumns = 2;

		GridData data = new GridData(GridData.FILL_BOTH);

		listExamples = new List(container, SWT.H_SCROLL | SWT.BORDER);
		listExamples.setLayoutData(data);

		data = new GridData(GridData.FILL_BOTH);
		
		selectedDescription = new Browser(container, SWT.NONE | SWT.BORDER);
		selectedDescription.setLayoutData(data);

		// get RSS from Web
		URL url;
		try {
			url = new URL("http://club.ncl.org.br/?q=rss.xml");
			rssReader = new RSSReader(url);
			rssReader.parse();
			items = rssReader.getRssRoot().getAllItems();

			// build the list from RSS
			for (int i = 0; i < items.size(); i++) {
				listExamples.add(items.get(i).getTitle());
			}

			selectedDescription
					.setText("Select an example to view its description.");
			selectedDescription.setSize(200, 200);

			listExamples.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int selectedItem = listExamples.getSelectionIndex();
					if (selectedItem == -1)
						return;
					String descr = "";
					descr = "<img src='" + 
									items.get(selectedItem).getImageUrl().toString()+"'/>";
					
					descr += items.get(selectedItem).getDescription();
								
					selectedDescription.setText(descr);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // TODO: transform in dynamic
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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