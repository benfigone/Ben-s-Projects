package gitlet;
import java.io.File;
import static gitlet.Utils.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;



/** Represents a gitlet repository.
 *
 *  does at a high level.
 *
 *  @author Ben Figone
 */
public class Repository implements Serializable {
    /**
     *
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");

    public static final File STAGED = Utils.join(GITLET_DIR, "staged");

    public static final File COMMIT = Utils.join(GITLET_DIR, "commit");

    public static final File HEAD = Utils.join(GITLET_DIR, "Head.txt");


    public static final File MASTER = Utils.join(GITLET_DIR, "Master.txt");

    public static final File STAGED_FOR_REMOVAL = Utils.join(STAGED, "stagedForRemoval.txt");

    public static final File STAGED_FOR_ADDITION = Utils.join(STAGED, "stagedForAddition.txt");

    public static final File BLOB = Utils.join(GITLET_DIR, "blob");

    public static final File BRANCHES = Utils.join(GITLET_DIR, "branches");

    public static final File CURRENT_BRANCH = Utils.join(GITLET_DIR, "currentBranch");

    public static void setClear() {
        HashMap<String, String> addFiles = new HashMap<String, String>();
        Utils.writeObject(STAGED_FOR_ADDITION, addFiles);
    }

    public static void setClear2() {
        ArrayList<String> removeFile = new ArrayList<String>();
        Utils.writeObject(STAGED_FOR_REMOVAL, removeFile);
    }


    public static void init() throws IOException {

        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            return;
        }


        GITLET_DIR.mkdir(); //try-catch statement to make files

        STAGED.mkdir();

        BLOB.mkdir();

        COMMIT.mkdir();

        BRANCHES.mkdir();

        STAGED_FOR_ADDITION.createNewFile();

        STAGED_FOR_REMOVAL.createNewFile();

        HEAD.createNewFile();

        MASTER.createNewFile();

        CURRENT_BRANCH.createNewFile();




        HashMap<String, String> addFiles = new HashMap<>();
        Utils.writeObject(STAGED_FOR_ADDITION, addFiles);
        ArrayList<String> removeFile = new ArrayList<String>();
        Utils.writeObject(STAGED_FOR_REMOVAL, removeFile);

        Commit init = new Commit("initial commit", null, new HashMap<>());
        String sha1 = init.getCommit();
        File newFile = Utils.join(COMMIT, sha1);
        newFile.createNewFile();
        Utils.writeObject(newFile, init);


        Utils.writeContents(HEAD, sha1);
        Utils.writeContents(MASTER, sha1);
        Utils.writeContents(CURRENT_BRANCH, "main");

        File file = Utils.join(BRANCHES, "main");
        file.createNewFile();
        Utils.writeContents(file, Utils.readContentsAsString(HEAD));

    }


    public static void add(String fileName) {

        File file = Utils.join(CWD, fileName);

        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        HashMap<String, String> addFiles = Utils.readObject(STAGED_FOR_ADDITION, HashMap.class);
        ArrayList<String> removeFile = Utils.readObject(STAGED_FOR_REMOVAL, ArrayList.class);
        String sha1first = Utils.readContentsAsString(file);
        String sha1Head = Utils.readContentsAsString(HEAD) /* + “.txt” */;

        Commit commitobj = Utils.readObject(Utils.join(COMMIT, sha1Head), Commit.class);
        String sha1second = commitobj.getBlob(fileName);

        if (commitobj.getParent() != null && commitobj.getBlobHash().keySet().contains(fileName)) {
            if (sha1second.equals(sha1first)) {
                removeFile.clear();
                Utils.writeObject(STAGED_FOR_REMOVAL, removeFile);
                return;
            }
        }

        if (removeFile.contains(fileName)) {
            removeFile.remove(fileName);
            return;
        }

        if (addFiles.containsKey(fileName)) {
            addFiles.remove(fileName);
            addFiles.put(fileName, Utils.readContentsAsString(file));
            Utils.writeObject(STAGED_FOR_ADDITION, addFiles);
        } else {
            addFiles.put(fileName, Utils.readContentsAsString(file));
            Utils.writeObject(STAGED_FOR_ADDITION, addFiles);
        }
    }


    public static void rm(String filename) {
        String sha1Head = Utils.readContentsAsString(HEAD);
        Commit commitobj = Utils.readObject(Utils.join(COMMIT, sha1Head), Commit.class);
        HashMap<String, String> addFiles = Utils.readObject(STAGED_FOR_ADDITION, HashMap.class);
        ArrayList<String> removeFile = Utils.readObject(STAGED_FOR_REMOVAL, ArrayList.class);


        if (!Utils.readObject(STAGED_FOR_ADDITION, HashMap.class).containsKey(filename)
                && !commitobj.getBlobHash().containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        if (Utils.readObject(STAGED_FOR_ADDITION, HashMap.class).containsKey(filename)) {
            addFiles.remove(filename);
        }

        if (commitobj.getBlobHash().containsKey(filename)) {
            removeFile.add(filename);
            restrictedDelete(Utils.join(CWD, filename));
        }
        Utils.writeObject(STAGED_FOR_ADDITION, addFiles);
        Utils.writeObject(STAGED_FOR_REMOVAL, removeFile);
    }


    public static void commit(String message) throws IOException {
        if (Utils.readObject(STAGED_FOR_ADDITION, HashMap.class).size() == 0
                && Utils.readObject(STAGED_FOR_REMOVAL, ArrayList.class).size() == 0) {
            System.out.print("No changes added to the commit.");
            return;
        }
        if (message.equals("")) {
            System.out.print("Please enter a commit message.");
            return;
        }

        //get current commit
        String sha1Head = Utils.readContentsAsString(HEAD) /* + “.txt” */;
        //create commit object of most current commit -- sha1 of commit
        Commit commitobj = Utils.readObject(Utils.join(COMMIT, sha1Head), Commit.class);


        //create new hashmap and copy it over
        HashMap<String, String> newHashMapBlob = new HashMap<>();
        newHashMapBlob.putAll(commitobj.getBlobHash());


        HashMap<String, String> hashMapFromAdd =
                Utils.readObject(STAGED_FOR_ADDITION, HashMap.class);
        Iterator<Map.Entry<String, String>> iterator2 = hashMapFromAdd.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry<String, String> entry = iterator2.next();
            String key = entry.getKey();
            String value = entry.getValue();
            //check if blob already exists
            if (newHashMapBlob.containsKey(key)) {
                //remove old blob
                newHashMapBlob.remove(key);
            }

            newHashMapBlob.put(key, value);
        }

        Commit newestCommit = new Commit(message, commitobj, newHashMapBlob);
        String sha1 = newestCommit.getCommit();
        File newfile = Utils.join(COMMIT, sha1);
        newfile.createNewFile();
        Utils.writeObject(newfile, newestCommit);


        //update Head and Master

        Utils.writeContents(HEAD, sha1);
        Utils.writeContents(MASTER, sha1);
        File newBranchUpdate = Utils.join(BRANCHES, Utils.readContentsAsString(CURRENT_BRANCH));
        Utils.writeContents(newBranchUpdate, sha1);



        //make blob object for commit
        String sha1Name = Utils.readContentsAsString(HEAD) /* + “.txt” */;
        File blobObject = Utils.join(BLOB, sha1Name);
        blobObject.createNewFile();
        Utils.writeObject(blobObject, newestCommit.getBlobHash());
        //clear both staging areas
        setClear();
        setClear2();

    }

    public static void checkoutFileHelper(String fileName, Commit commit) {
        //overwrites contents of file into cwd
        File temp = Utils.join(CWD, fileName);
        writeContents(temp, commit.getBlob(fileName));
    }

    public static void checkout1(String fileName) {
        String sha1Head = Utils.readContentsAsString(HEAD) /* + “.txt” */;
        Commit commitobj = Utils.readObject(Utils.join(COMMIT, sha1Head), Commit.class);
        if (commitobj.getBlob(fileName) == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        checkoutFileHelper(fileName, commitobj);
    }

    public static void checkout2(String commitId, String fileName) {
        //go through commits folder
        List<String> koahla = Utils.plainFilenamesIn(COMMIT);
        if (commitId.length() == 8) {
            for (String i : koahla) {
                if (i.startsWith(commitId)) {
                    commitId = i;
                    break;
                }
            }
        }
        if (!koahla.contains(commitId)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        File file = Utils.join(COMMIT, commitId /* + “.txt” */);
        Commit commitobj = Utils.readObject(file, Commit.class);
        if (!commitobj.getBlobHash().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        checkoutFileHelper(fileName, commitobj);
    }


    public static void checkout3(String branchname) throws IOException {
        List<String> t  = Utils.plainFilenamesIn(BRANCHES);

        //if working file is untracked
        if (!t.contains(branchname)) {
            System.out.println("No such branch exists.");
            return;
        }

        //if any file that exist present in CWD not present in current branch
        Set<String> filesCWD = new HashSet<>();
        filesCWD.addAll(Utils.plainFilenamesIn(CWD));
        String sha1Head = Utils.readContentsAsString(HEAD);
        Commit commitobj = Utils.readObject(Utils.join(COMMIT, sha1Head), Commit.class);
        Set<String> commmitBlobSet = commitobj.getBlobHash().keySet();
        Set<String> currentCommit = new HashSet<>();
        currentCommit.addAll(commmitBlobSet);




        //check if branch is current branch
        if (Utils.readContentsAsString(CURRENT_BRANCH).equals(branchname)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }



        List<String> commitFiles = Utils.plainFilenamesIn(COMMIT);



        for (String i : commitFiles) {
            //compare contents of current to contents of CWD
            if ((!commmitBlobSet.contains(i) && filesCWD.contains(i))) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                return;
            }
        }

        File temp = Utils.join(BRANCHES, branchname);
        String sha1OtherBranch = Utils.readContentsAsString(temp);
        Commit commitobj2 = Utils.readObject(Utils.join(COMMIT, sha1OtherBranch), Commit.class);
        HashMap<String, String> hashMap2 = commitobj2.getBlobHash();
        Set<String> keys = hashMap2.keySet();
        for (String i : keys) {
            checkoutFileHelper(i, commitobj2);
        }
        for (String i : currentCommit) {
            if (!keys.contains(i)) {
                File temp2 = Utils.join(CWD, i);
                temp2.delete();
            }
        }
        Utils.writeContents(CURRENT_BRANCH, branchname);
        setClear(); //clear addition
        setClear2(); //clear removal
    }


    public static void log() {
        String sha1Head = Utils.readContentsAsString(HEAD) /* + “.txt” */;
        Commit commitobj = Utils.readObject(Utils.join(COMMIT, sha1Head), Commit.class);
        System.out.println("===");
        System.out.println("commit " + sha1Head);
        System.out.println("Date: " + commitobj.getDate());
        System.out.println(commitobj.getMessage());
        System.out.println();
        commitobj = commitobj.getParent();
        while (commitobj != null) {
            String temp = commitobj.getCommit();
            System.out.println("===");
            System.out.println("commit " + temp);
            System.out.println("Date: " + commitobj.getDate());
            System.out.println(commitobj.getMessage());
            System.out.println();
            commitobj = commitobj.getParent();
        }
    }

    public static void find(String commitMessage) {
        List<String> listOfFiles = Utils.plainFilenamesIn(COMMIT);
        int counter = 0;
        for (String file: listOfFiles) {
            Commit curr = Utils.readObject(Utils.join(COMMIT, file), Commit.class);
            if (curr.getMessage().equals(commitMessage)) {
                counter++;
                System.out.println(curr.getCommit());
            }
        }
        if (counter == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }

    }

    public static void globalLog() {
        List<String> listOfFiles = Utils.plainFilenamesIn(COMMIT);
        for (String file: listOfFiles) {
            Commit curr = Utils.readObject(Utils.join(COMMIT, file), Commit.class);
            System.out.println("===");
            System.out.println("commit " + file);
            System.out.println("Date: " + curr.getDate());
            System.out.println(curr.getMessage());
            System.out.println();
        }
    }

    public static void branch(String nameBranch) throws IOException {
        File file = Utils.join(BRANCHES, nameBranch);
        if (file.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        String sha1Head = Utils.readContentsAsString(HEAD);
        file.createNewFile();
        Utils.writeContents(file, sha1Head);
        //head sha1
    }

    public static void removeBranch(String nameBranch) {
        File temp = Utils.join(BRANCHES, nameBranch);
        if (!temp.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (nameBranch.equals(Utils.readContentsAsString(CURRENT_BRANCH))) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        } else {
            temp.delete();
        }
    }
    public static void status() {

        List<String> listOfBranches = Utils.plainFilenamesIn(BRANCHES);
        System.out.println("=== Branches ===");
        for (String branch : listOfBranches) {
            if (branch.equals(Utils.readContentsAsString(CURRENT_BRANCH))) {
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        HashMap stage = Utils.readObject(STAGED_FOR_ADDITION, HashMap.class);
        ArrayList<String> sortedKeys = new ArrayList<String>(stage.keySet());
        Collections.sort(sortedKeys);
        for (String key : sortedKeys) {
            System.out.println(key);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        ArrayList<String> rm = Utils.readObject(STAGED_FOR_REMOVAL, ArrayList.class);
        for (String i : rm) {
            System.out.println(i);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
    }

    public static void merge(String branchName) {
        HashMap<String, String> addFiles = Utils.readObject(STAGED_FOR_ADDITION, HashMap.class);
        ArrayList<String> removeFile = Utils.readObject(STAGED_FOR_REMOVAL, ArrayList.class);
        File newBranchUpdate = Utils.join(BRANCHES, Utils.readContentsAsString(CURRENT_BRANCH));
        if (addFiles.size() > 0 || removeFile.size() > 0) {
            System.out.println("You have uncommitted changes.");
            return;
        } else if (!Utils.plainFilenamesIn(BRANCHES).contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
        } else if (branchName.equals(newBranchUpdate)) {
            System.out.println("Cannot merge a branch with itself.");
        }
    }

    public static void reset(String commitID) {

        //Utils.plainFilenamesIn(CWD); --> refers to the files in the cwd
        //gets current commit object
        List<String> listOfCommits = Utils.plainFilenamesIn(COMMIT);
        String sha1Head = Utils.readContentsAsString(HEAD);
        Commit headcommit = Utils.readObject(Utils.join(COMMIT, sha1Head), Commit.class);

        if (!listOfCommits.contains(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }

        //if it exists in the current and cwd, but not in the commit you want, delete (erase)
        //if the file is in the current commit, but not in the cwd or commit you want then it's fine
        //
        for (String i: listOfCommits) {
            if (i.substring(0, 5).equals(commitID.substring(0, 5)) || i.equals(commitID)) {
                File temp = Utils.join(COMMIT, commitID);
                Commit otherCommmit = readObject(temp, Commit.class);
                String sha1 = headcommit.getCommit();
                for (String j: otherCommmit.getBlobHash().keySet()) {
                    File f = Utils.join(CWD, j);
                    if (!headcommit.getBlobHash().containsKey(j) && f.exists()) {
                        //restrictedDelete(Utils.join(CWD, j));
                        System.out.println("There is an untracked file in the way; "
                                + "delete it, or add and commit it first.");
                        System.exit(0);
                    }
                }
                //go thru all the current files in current commit, if it doesnt exist in the
                //reset the current branch
                for (String k: headcommit.getBlobHash().keySet()) {
                    if (headcommit.getBlobHash().containsKey(k)) {
                        restrictedDelete(Utils.join(CWD, k));
                    }
                }
                for (String j: otherCommmit.getBlobHash().keySet()) {
                    File f = Utils.join(CWD, j);
                    Utils.writeContents(f, otherCommmit.getBlobHash().get(j));
                }

                File newBranchUpdate = Utils.join(BRANCHES,
                        Utils.readContentsAsString(CURRENT_BRANCH));
                Utils.writeContents(newBranchUpdate, i);
            }
        }
        setClear();
        setClear2();
    }
}
