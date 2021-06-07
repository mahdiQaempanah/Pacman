package sample.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.Model.Account;
import sample.Model.ServerMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MainController {
    private static final MainController mainController = new MainController();
    private static final String databaseAddress = "src\\main\\resources\\NotCode\\Database\\";
    private Account loggingUser;


    public static MainController getInstance() {
        return mainController;
    }

    public ServerMessage attemptForRegister(String username, String password) throws IOException {
        HashMap<String, Object> ans = new HashMap<>();

        ArrayList<Account> accounts = getAccounts();
        for (Account account : accounts) {
            if (account.getUsername().equals(username)) {
                ans.put("detail", "account with this username is already exist");
                return new ServerMessage(ServerMessage.error, ans);
            }
        }

        addAccountToDatabase(new Account(username, password));
        return new ServerMessage(ServerMessage.successful, ans);
    }

    private void addAccountToDatabase(Account newAccount) throws IOException {
        ArrayList<Account> accounts = getAccounts();
        FileWriter fileWriter = new FileWriter(databaseAddress + "Accounts.txt");
        accounts.add(newAccount);
        fileWriter.write(new Gson().toJson(accounts));
        fileWriter.close();
    }

    public Account getLoggingUser() {
        return this.loggingUser;
    }

    public boolean hasLoggingAccountLoadGame() {
        return loggingUser.getLastGame() != null;
    }

    private ArrayList<Account> getAccounts() throws IOException {
        System.out.println(Paths.get(databaseAddress + "Accounts.txt").toAbsolutePath());
        String json = new String(Files.readAllBytes(Paths.get(databaseAddress + "Accounts.txt")));
        return new Gson().fromJson(json, new TypeToken<List<Account>>() {
        }.getType());
    }

    public ServerMessage attemptForLogin(String username, String password) throws IOException {
        HashMap<String, Object> ans = new HashMap<>();
        ArrayList<Account> accounts = getAccounts();
        for (Account account : accounts) {
            if (account.getUsername().equals(username)) {
                if (account.getPassword().equals(password)) {
                    setLoggingUser(account);
                    System.out.println(loggingUser);
                    return new ServerMessage(ServerMessage.successful, ans);
                } else {
                    ans.put("detail", "password or username is invalid");
                    return new ServerMessage(ServerMessage.error, ans);
                }
            }
        }
        ans.put("detail", "password or username is invalid");
        return new ServerMessage(ServerMessage.error, ans);
    }

    private void setLoggingUser(Account account) {
        this.loggingUser = account;
    }

    public void logout() {
        loggingUser = null;
    }

    public void deleteLoggingAccount() throws IOException {
        deleteAccountFromDatabase(loggingUser);
    }

    public void saveChangeForAccount(Account account) throws IOException {
        changeAccountInfoInDataBase(account);
    }

    private void deleteAccountFromDatabase(Account deleteAccount) throws IOException {
        ArrayList<Account> accounts = getAccounts();
        FileWriter fileWriter = new FileWriter(databaseAddress + "Accounts.txt");
        for (Account account : accounts) {
            if (account.getUsername().equals(deleteAccount.getUsername())) {
                accounts.remove(account);
                break;
            }
        }
        fileWriter.write(new Gson().toJson(accounts));
        fileWriter.close();
    }

    private void changeAccountInfoInDataBase(Account newAccountInfo) throws IOException {
        ArrayList<Account> accounts = getAccounts();
        FileWriter fileWriter = new FileWriter(databaseAddress + "Accounts.txt");
        for (Account account : accounts) {
            if (account.getUsername().equals(newAccountInfo.getUsername())) {
                accounts.remove(account);
                accounts.add(newAccountInfo);
                break;
            }
        }
        fileWriter.write(new Gson().toJson(accounts));
        fileWriter.close();
    }

    public ServerMessage getTopUsers(int requestedUsers) throws IOException {
        HashMap<String, Object> ans = new HashMap<>();
        Account[] accounts = getAccounts().toArray(new Account[0]);
        requestedUsers = Math.min(requestedUsers, accounts.length);
        Arrays.sort(accounts, (o1, o2) -> {
            if (o1.getMaxScore() != o2.getMaxScore())
                return o2.getMaxScore() - o1.getMaxScore();
            return (int) (o1.getLastGameTime() - o2.getLastGameTime());
        });

        ArrayList<Account> topUsers = new ArrayList<>(Arrays.asList(accounts).subList(0, requestedUsers));
        ans.put("topUsers", topUsers);
        return new ServerMessage(ServerMessage.successful, ans);
    }

    public ServerMessage changePassword(String password, String newPassword) throws IOException {
        HashMap<String, Object> ans = new HashMap<>();
        System.out.println(loggingUser);
        if (!loggingUser.getPassword().equals(password)) {
            ans.put("reason", "password invalid");
            return new ServerMessage(ServerMessage.error, ans);
        }
        loggingUser.setPassword(newPassword);
        changeAccountInfoInDataBase(loggingUser);
        return new ServerMessage(ServerMessage.successful, ans);
    }
}
