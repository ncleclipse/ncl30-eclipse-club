package br.ufma.deinf.laws.ncleclipse.club.wizards;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class NCLClubNewWizard extends Wizard implements INewWizard {
	private NCLClubListExamplesPage pageList;
	private NCLClubNewWizardPage page;
	private ISelection selection;
	private IProject project;
	private String containerName;

	/**
	 * Constructor for NCLClubNewWizard.
	 */
	public NCLClubNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		pageList = new NCLClubListExamplesPage(selection);
		page = new NCLClubNewWizardPage(selection);
		addPage(pageList);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		containerName = page.getContainerName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(containerName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private void doFinish(String containerName,
			IProgressMonitor monitor) throws CoreException {
		// create a sample file
		monitor.beginTask("Creating project " + containerName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(containerName);

		// create the project and open it
		if (!project.exists()) {
			project.create(monitor);
			if(!project.isOpen()) project.open(monitor);
		}
		/*
		 * try { InputStream stream = openContentStream(); if (file.exists()) {
		 * file.setContents(stream, true, true, monitor); } else {
		 * file.create(stream, true, monitor); } stream.close(); } catch
		 * (IOException e) { }
		 */
		monitor.worked(1);
		try {
			createResourcesFromUrl(pageList.getSelectedItem().getResourcesZipUrl(), monitor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 root.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		/*
		 * getShell().getDisplay().asyncExec(new Runnable() { public void run()
		 * { IWorkbenchPage page =
		 * PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		 * try { IDE.openEditor(page, file, true); } catch (PartInitException e)
		 * { } } });
		 */
		// monitor.worked(1);
	}

	/**
	 * 
	 * @param url
	 * @throws IOException 
	 * @throws CoreException 
	 */
	private void createResourcesFromUrl(URL url, IProgressMonitor monitor) throws IOException, CoreException {
		monitor.setTaskName("Getting zip file...");
		InputStream is = url.openStream();
		monitor.worked(1);
		createResourcesFromZip(is, monitor);
	}

	/**
	 * 
	 * @param in
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws CoreException 
	 */
	private void createResourcesFromZip(InputStream in, IProgressMonitor monitor) throws FileNotFoundException, IOException, CoreException {
		ZipInputStream zis = new ZipInputStream(in);
		ZipEntry entry;
		while((entry = zis.getNextEntry()) != null){
			monitor.setTaskName("Unzipping: " + entry.getName());
			
			int size;
			byte[] buffer = new byte[2048];

			monitor.worked(1);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(containerName);
			
			System.out.println(project.getFullPath().toOSString() + "/" +entry.getName());
			IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
			
			if(entry.isDirectory()){
				IFolder folder = project.getFolder(entry.getName());
				if(!folder.exists()) folder.create(IResource.NONE, true, null);
				continue;
			}

			System.out.println(workspacePath.toOSString());
			
			FileOutputStream fos = new FileOutputStream(workspacePath.toOSString()+"/"+project.getFullPath()+"/" + entry.getName());
			BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
			while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
				  bos.write(buffer, 0, size);
			}
			bos.flush();
			bos.close(); 
		}
		zis.close();
	}

	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream() {
		String contents = "This is the initial file contents for *.mpe file that should be word-sorted in the Preview page of the multi-page editor";
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "ncl30_eclipse_club",
				IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}