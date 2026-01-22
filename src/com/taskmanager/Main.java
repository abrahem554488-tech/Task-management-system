package com.taskmanager;

// Main.java - واجهة المستخدم الرئيسية
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        System.out.println("======== نظام إدارة المهام المتقدم ========");
        System.out.println("     مع إمكانية التراجع (Undo) عن العمليات");
        System.out.println("===========================================");

        while (true) {
            displayMenu();
            System.out.print("اختر الخيار (1-9): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        addNewTask(scanner, taskManager);
                        break;
                    case 2:
                        viewAllTasks(taskManager);
                        break;
                    case 3:
                        updateExistingTask(scanner, taskManager);
                        break;
                    case 4:
                        deleteTask(scanner, taskManager);
                        break;
                    case 5:
                        toggleTaskStatus(scanner, taskManager);
                        break;
                    case 6:
                        searchForTasks(scanner, taskManager);
                        break;
                    case 7:
                        viewFilteredTasks(scanner, taskManager);
                        break;
                    case 8:
                        undoLastOperation(taskManager);
                        break;
                    case 9:
                        System.out.println("شكراً لاستخدام نظام إدارة المهام!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("خيار غير صالح! الرجاء المحاولة مرة أخرى.");
                }
            } catch (NumberFormatException e) {
                System.out.println("الرجاء إدخال رقم صحيح!");
            }

            System.out.println("\nاضغط Enter للمتابعة...");
            scanner.nextLine();
        }
    }

    private static void displayMenu() {
        System.out.println("\n===== القائمة الرئيسية =====");
        System.out.println("1. إضافة مهمة جديدة");
        System.out.println("2. عرض جميع المهام");
        System.out.println("3. تحديث مهمة موجودة");
        System.out.println("4. حذف مهمة");
        System.out.println("5. تغيير حالة المهمة");
        System.out.println("6. بحث في المهام");
        System.out.println("7. عرض المهام المصفاة");
        System.out.println("8. التراجع عن آخر عملية");
        System.out.println("9. خروج");
        System.out.println("===========================");
    }

    private static void addNewTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("\n--- إضافة مهمة جديدة ---");
        System.out.print("أدخل عنوان المهمة: ");
        String title = scanner.nextLine();

        System.out.print("أدخل وصف المهمة: ");
        String description = scanner.nextLine();

        taskManager.addTask(title, description);
    }

    private static void viewAllTasks(TaskManager taskManager) {
        System.out.println("\n--- عرض جميع المهام ---");
        taskManager.displayTasks();
    }

    private static void updateExistingTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("\n--- تحديث مهمة ---");
        taskManager.displayTasks();

        System.out.print("أدخل رقم المهمة للتحديث: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("أدخل العنوان الجديد: ");
        String newTitle = scanner.nextLine();

        System.out.print("أدخل الوصف الجديد: ");
        String newDescription = scanner.nextLine();

        taskManager.updateTask(id, newTitle, newDescription);
    }

    private static void deleteTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("\n--- حذف مهمة ---");
        taskManager.displayTasks();

        System.out.print("أدخل رقم المهمة للحذف: ");
        int id = Integer.parseInt(scanner.nextLine());

        taskManager.deleteTask(id);
    }

    private static void toggleTaskStatus(Scanner scanner, TaskManager taskManager) {
        System.out.println("\n--- تغيير حالة المهمة ---");
        taskManager.displayTasks();

        System.out.print("أدخل رقم المهمة لتغيير حالتها: ");
        int id = Integer.parseInt(scanner.nextLine());

        taskManager.toggleTaskStatus(id);
    }

    private static void searchForTasks(Scanner scanner, TaskManager taskManager) {
        System.out.println("\n--- بحث في المهام ---");
        System.out.print("أدخل كلمة البحث: ");
        String keyword = scanner.nextLine();

        taskManager.searchTasks(keyword);
    }

    private static void viewFilteredTasks(Scanner scanner, TaskManager taskManager) {
        System.out.println("\n--- عرض المهام المصفاة ---");
        System.out.println("1. عرض المهام المكتملة فقط");
        System.out.println("2. عرض المهام المعلقة فقط");
        System.out.print("اختر نوع العرض: ");

        int filterChoice = Integer.parseInt(scanner.nextLine());

        switch (filterChoice) {
            case 1:
                taskManager.displayCompletedTasks();
                break;
            case 2:
                taskManager.displayPendingTasks();
                break;
            default:
                System.out.println("خيار غير صالح!");
        }
    }

    private static void undoLastOperation(TaskManager taskManager) {
        System.out.println("\n--- التراجع عن آخر عملية ---");
        taskManager.undo();
    }
}