import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DeleteFilesInDir {
	
	public static void main(String[] args) throws Exception {
		String sourceFilename = args[0];
		String dirname = args[1];
		
		Scanner s = new Scanner(new File(sourceFilename));
		ArrayList<String> filenamesToDelete = new ArrayList<>();
		while (s.hasNextLine()){
			String filenameToDelete = s.nextLine().trim();
			filenamesToDelete.add(filenameToDelete);
		}
		s.close();
		
		System.out.println("Deleting " + filenamesToDelete.size() + " files...");
		
		File dir = new File(dirname);
		File[] filesInDir = dir.listFiles();
		int filesDeletedCounter = 0;
		int skippedFiles = 0;
		if (filesInDir != null) {
			for (File fileInDir : filesInDir) {
				String filenameInDir = fileInDir.getName();
				if (filenamesToDelete.contains(filenameInDir)) {
					boolean deleted = fileInDir.delete();
					if (deleted) {
						filesDeletedCounter++;
					} else {
						System.out.println("Failed to delete file " + filenameInDir);
					}
				} else {
					skippedFiles++;
				}
			}
		}
		
		System.out.println("Deleted " + filesDeletedCounter + " files!");
		System.out.println("Skipped " + skippedFiles + " files!");
	}

}

