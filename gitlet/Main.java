package gitlet;
import java.io.IOException;
import static gitlet.Repository.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Victor Cruz Ramos and Ben Figone
 */
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.print("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                init();
                break;
            case "add":
                checkInit();
                validateNumArgs("add", args, 2);
                Repository.add(args[1]);
                break;
            case "checkout":
                checkInit();
                if (args[1].equals("--") && args.length == 3) {
                    checkout1(args[2]);
                } else if (args.length == 4 && args[2].equals("--")) {
                    checkout2(args[1], args[3]);
                } else if (args.length == 2) {
                    checkout3(args[1]);
                } else {
                    System.out.println("Incorrect operands.");
                }
                break;
            case "commit":
                checkInit();
                validateNumArgs("commit", args, 2);
                commit(args[1]);
                break;
            case "log":
                checkInit();
                validateNumArgs("log", args, 1);
                log();
                break;
            case "find":
                checkInit();
                validateNumArgs("find", args, 2);
                find(args[1]);
                break;
            case "global-log":
                checkInit();
                validateNumArgs("global-log", args, 1);
                globalLog();
                break;
            case "branch":
                checkInit();
                validateNumArgs("branch", args, 2);
                branch(args[1]);
                break;
            case "rm":
                checkInit();
                validateNumArgs("rm", args, 2);
                rm(args[1]);
                break;
            case "rm-branch":
                checkInit();
                validateNumArgs("rm-branch", args, 2);
                removeBranch(args[1]);
                break;
            case "status":
                checkInit();
                validateNumArgs("status", args, 1);
                status();
                break;
            case "merge":
                break;
            case "reset":
                checkInit();
                validateNumArgs("reset", args, 2);
                reset(args[1]);
                break;
            default:
                exitWithError("No command with that name exists.");
        }
        return;
    }
    public static void checkInit() {
        if (!GITLET_DIR.exists()) {
            exitWithError("Not in an initialized Gitlet directory.");
        }
    }
    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(0);
    }
    public static boolean checkNumInputs(int reqLength, String[] args) {
        if (args.length == reqLength) {
            return true;
        } else {
            System.out.println("Incorrect operands.");
            return false;
        }
    }
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            System.exit(0);
        }
    }
}
