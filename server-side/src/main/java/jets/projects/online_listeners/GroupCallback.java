package jets.projects.online_listeners;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.GroupDao;
import jets.projects.dao.GroupMemberDao;
import jets.projects.entities.Group;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class GroupCallback {
    private static ExecutorService executor;
    
    private static final GroupDao groupDao 
            = new GroupDao();
    private static final GroupMemberDao groupMemberDao 
            = new GroupMemberDao();
    
    private static boolean isInit = false;
    public GroupCallback() {
        if (isInit) {
            throw new UnsupportedOperationException(
                    "Object has already been init.");
        }
        isInit = true;
    }
    
    public void start() {
        if (executor != null) {
            throw new IllegalStateException(
                    "The executor is already running.");
        }
        executor = MyExecutorFactory.getExecutorService();
    }
    
    public void shutDown() {
        if (executor == null) {
            throw new IllegalStateException(
                    "The executor is already shutdown.");
        }
        try {
            executor.shutdown();
            if (!executor.awaitTermination(
                    Delays.EXECUTOR_AWAIT_TERMINATION_TIMEOUT,
                    TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            System.err.println("Thread interrupted while waiting "
                    + "to terminate the executor.");
        } finally {
            executor = null;
        }
    }
    
    public static void groupPicChanged(int groupID, byte[] newPic) {
        executor.submit(() -> {
            var table = OnlineNormalUserTable.table;
           
            var groupMembersIDsResult 
                    = groupMemberDao.getGroupMembersIDs(groupID);
            if (groupMembersIDsResult.getErrorMessage() != null) {
                System.err.println(
                        groupMembersIDsResult.getErrorMessage());
                return;
            }
            List<Integer> membersIDs 
                    = groupMembersIDsResult.getResponseData();
            
            for (int ID : membersIDs) {
                var user = table.getOrDefault(ID, null);
                if (user == null) {
                    continue;
                }

                try {
                    user.getImpl().groupPicChanged(groupID, newPic);
                } catch (RemoteException ex) {
                    System.err.println("Callback Error: " 
                            + ex.getMessage());
                }
            }
        });
    }

    public static void addedToGroup(int userID, int groupID) {
        executor.submit(()-> {
            var table = OnlineNormalUserTable.table;
            var receiverUser = table.getOrDefault(
                    userID, null);
            
            // User is offline.
            if (receiverUser == null) {
                return;
            }
            
            var groupInfoResult = groupDao.getGroupById(groupID);
            if (groupInfoResult.getErrorMessage() != null) {
                System.err.println(groupInfoResult.getErrorMessage());
                return;
            }
            Group group = groupInfoResult.getResponseData();
            
            try {
                receiverUser.getImpl().addedToGroup(group);
            } catch (RemoteException ex) {
                System.err.println("Callback Error: " 
                        + ex.getMessage());
            }
        });
    }
    
    public static void removedFromGroup(int userID, int groupID) {
        executor.submit(()-> {
            var table = OnlineNormalUserTable.table;
            var receiverUser = table.getOrDefault(
                    userID, null);
            
            // User is offline.
            if (receiverUser == null) {
                return;
            }
            
            try {
                receiverUser.getImpl().removedFromGroup(groupID);
            } catch (RemoteException ex) {
                System.err.println("Callback Error: " 
                        + ex.getMessage());
            }
        });        
    }
    
    public static void leadershipGained(int userID, int groupID) {
        executor.submit(()-> {
            var table = OnlineNormalUserTable.table;
            var receiverUser = table.getOrDefault(
                    userID, null);
            
            // User is offline.
            if (receiverUser == null) {
                return;
            }
            
            try {
                receiverUser.getImpl().leadershipGained(groupID);
            } catch (RemoteException ex) {
                System.err.println("Callback Error: " 
                        + ex.getMessage());
            }
        });       
    }
    
    public static void groupMemberLeft(int groupID, int memberID) {
        executor.submit(()-> {
            var table = OnlineNormalUserTable.table;

            var groupMembersIDsResult 
                    = groupMemberDao.getGroupMembersIDs(groupID);
            if (groupMembersIDsResult.getErrorMessage() != null) {
                System.err.println(
                        groupMembersIDsResult.getErrorMessage());
                return;
            }
            List<Integer> membersIDs 
                    = groupMembersIDsResult.getResponseData();
            
            for (int ID : membersIDs) {
                var user = table.getOrDefault(ID, null);
                if (user == null) {
                    continue;
                }
                
                try {
                    user.getImpl().groupMemberLeft(groupID, memberID);
                } catch (RemoteException ex) {
                    System.err.println("Callback Error: "
                        + ex.getMessage());
                }
            }
        });
    }
    
    // Exclude the admin and the affected from the called list in the 
    // following functions.
    public static void newGroupMemberAdded(int adminID,
            GroupMemberInfo newMember) {
        executor.submit(()-> { 
            int newMemberID = newMember.getMember().getMemberID();
            int groupID = newMember.getMember().getGroupID();
            var table = OnlineNormalUserTable.table;

            var groupMembersIDsResult 
                    = groupMemberDao.getGroupMembersIDs(groupID);
            if (groupMembersIDsResult.getErrorMessage() != null) {
                System.err.println(
                        groupMembersIDsResult.getErrorMessage());
                return;
            }
            List<Integer> membersIDs = 
                    groupMembersIDsResult.getResponseData();

            for (int ID : membersIDs) {
                if (ID == adminID || ID == newMemberID) {
                    continue;
                }

                var user = table.getOrDefault(ID, null);
                if (user == null) {
                    continue;
                }

                try {
                    user.getImpl().newGroupMemberAdded(newMember);
                } catch (RemoteException ex) {
                    System.err.println("Callback Error: "
                        + ex.getMessage());
                }
            }
        });
    }
    
    public static void groupMemberRemoved(int adminID,
            int groupID, int memberID) {
        executor.submit(()-> {
            var table = OnlineNormalUserTable.table;

            var groupMembersIDsResult 
                    = groupMemberDao.getGroupMembersIDs(groupID);
            if (groupMembersIDsResult.getErrorMessage() != null) {
                System.err.println(
                        groupMembersIDsResult.getErrorMessage());
                return;
            }
            List<Integer> membersIDs 
                    = groupMembersIDsResult.getResponseData();

            for (int ID : membersIDs) {
                if (ID == adminID) {
                    continue;
                }

                var user = table.getOrDefault(ID, null);
                if (user == null) {
                    continue;
                }

                try {
                    user.getImpl().groupMemberRemoved(groupID, memberID);
                } catch (RemoteException ex) {
                    System.err.println("Callback Error: "
                        + ex.getMessage());
                }
            }
        });
    }
    
    public static void adminChanged(int oldAdminID,
            int groupID, int newAdminID) {
        executor.submit(()-> {
            var table = OnlineNormalUserTable.table;

            var groupMembersIDsResult 
                    = groupMemberDao.getGroupMembersIDs(groupID);
            if (groupMembersIDsResult.getErrorMessage() != null) {
                System.err.println(
                        groupMembersIDsResult.getErrorMessage());
                return;
            }
            List<Integer> membersIDs 
                    = groupMembersIDsResult.getResponseData();

            for (int ID : membersIDs) {
                if (ID == oldAdminID || ID == newAdminID) {
                    continue;
                }

                var user = table.getOrDefault(ID, null);
                if (user == null) {
                    continue;
                }

                try {
                    user.getImpl().adminChanged(groupID, newAdminID);
                } catch (RemoteException ex) {
                    System.err.println("Callback Error: "
                        + ex.getMessage());
                }
            }
        });
    }
    
    public static void groupDeleted(int groupID, int adminID, 
            List<Integer> membersIDs) {
        executor.submit(()-> {
            var table = OnlineNormalUserTable.table;

            for (int ID : membersIDs) {
                if (ID == adminID) {
                    continue;
                }

                var user = table.getOrDefault(ID, null);
                if (user == null) {
                    continue;
                }

                try {
                    user.getImpl().groupDeleted(groupID);
                } catch (RemoteException ex) {
                    System.err.println("Callback Error: "
                        + ex.getMessage());
                }
            }
        });      
    }
}
