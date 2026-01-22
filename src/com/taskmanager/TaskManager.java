package com.taskmanager;

// TaskManager.java - النظام الرئيسي باستخدام Single Linked List
import java.util.Scanner;

public class TaskManager {
    private TaskNode head;
    private int nextId;

    // قائمة العمليات للتراجع
    private OperationNode undoStack;

    // عقدة لتخزين عملية للتراجع
    private class OperationNode {
        String operationType;
        Task taskData;
        OperationNode next;

        OperationNode(String operationType, Task task) {
            this.operationType = operationType;
            this.taskData = task != null ? task.copy() : null;
            this.next = null;
        }
    }

    public TaskManager() {
        this.head = null;
        this.nextId = 1;
        this.undoStack = null;
    }

    // إضافة عملية للتاريخ للتراجع
    private void pushOperation(String operationType, Task task) {
        OperationNode newOperation = new OperationNode(operationType, task);
        newOperation.next = undoStack;
        undoStack = newOperation;
    }

    // التراجع عن آخر عملية
    public boolean undo() {
        if (undoStack == null) {
            System.out.println("لا توجد عمليات للتراجع!");
            return false;
        }

        OperationNode lastOperation = undoStack;
        undoStack = undoStack.next;

        switch (lastOperation.operationType) {
            case "ADD":
                // التراجع عن الإضافة بحذف آخر مهمة
                removeLastAdded();
                break;
            case "DELETE":
                // التراجع عن الحذف بإعادة المهمة
                if (lastOperation.taskData != null) {
                    restoreTask(lastOperation.taskData);
                }
                break;
            case "UPDATE":
                // التراجع عن التحديث باستعادة النسخة القديمة
                if (lastOperation.taskData != null) {
                    restoreTask(lastOperation.taskData);
                }
                break;
            case "TOGGLE":
                // التراجع عن تغيير الحالة
                if (lastOperation.taskData != null) {
                    toggleTaskStatus(lastOperation.taskData.getId());
                }
                break;
        }

        System.out.println("تم التراجع عن العملية: " + lastOperation.operationType);
        return true;
    }

    // إضافة مهمة جديدة
    public void addTask(String title, String description) {
        Task newTask = new Task(nextId++, title, description);
        TaskNode newNode = new TaskNode(newTask);

        // حفظ العملية للتراجع
        pushOperation("ADD", newTask.copy());

        if (head == null) {
            head = newNode;
        } else {
            TaskNode current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }

        System.out.println("تمت إضافة المهمة بنجاح!");
    }

    // حذف آخر مهمة مضافة (لعملية التراجع)
    private void removeLastAdded() {
        if (head == null) return;

        if (head.getNext() == null) {
            head = null;
        } else {
            TaskNode current = head;
            while (current.getNext().getNext() != null) {
                current = current.getNext();
            }
            current.setNext(null);
        }
    }

    // حذف مهمة حسب المعرف
    public boolean deleteTask(int id) {
        if (head == null) {
            System.out.println("القائمة فارغة!");
            return false;
        }

        // إذا كانت المهمة الأولى
        if (head.getTask().getId() == id) {
            pushOperation("DELETE", head.getTask().copy());
            head = head.getNext();
            System.out.println("تم حذف المهمة بنجاح!");
            return true;
        }

        TaskNode current = head;
        while (current.getNext() != null && current.getNext().getTask().getId() != id) {
            current = current.getNext();
        }

        if (current.getNext() != null) {
            pushOperation("DELETE", current.getNext().getTask().copy());
            current.setNext(current.getNext().getNext());
            System.out.println("تم حذف المهمة بنجاح!");
            return true;
        }

        System.out.println("لم يتم العثور على المهمة!");
        return false;
    }

    // استعادة مهمة محذوفة
    private void restoreTask(Task task) {
        TaskNode newNode = new TaskNode(task.copy());

        if (head == null) {
            head = newNode;
        } else {
            TaskNode current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
    }

    // تحديث مهمة
    public boolean updateTask(int id, String newTitle, String newDescription) {
        TaskNode node = findTaskNode(id);
        if (node == null) {
            System.out.println("لم يتم العثور على المهمة!");
            return false;
        }

        // حفظ النسخة القديمة للتراجع
        pushOperation("UPDATE", node.getTask().copy());

        node.getTask().setTitle(newTitle);
        node.getTask().setDescription(newDescription);
        System.out.println("تم تحديث المهمة بنجاح!");
        return true;
    }

    // تبديل حالة المهمة (مكتملة/غير مكتملة)
    public boolean toggleTaskStatus(int id) {
        TaskNode node = findTaskNode(id);
        if (node == null) {
            System.out.println("لم يتم العثور على المهمة!");
            return false;
        }

        // حفظ الحالة السابقة للتراجع
        pushOperation("TOGGLE", node.getTask().copy());

        node.getTask().setCompleted(!node.getTask().isCompleted());
        System.out.println("تم تغيير حالة المهمة بنجاح!");
        return true;
    }

    // البحث عن مهمة بالمعرف
    private TaskNode findTaskNode(int id) {
        TaskNode current = head;
        while (current != null) {
            if (current.getTask().getId() == id) {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }

    // عرض جميع المهام
    public void displayTasks() {
        if (head == null) {
            System.out.println("لا توجد مهام حالياً!");
            return;
        }

        System.out.println("\n=== قائمة المهام ===");
        System.out.println("====================");

        TaskNode current = head;
        while (current != null) {
            System.out.println(current.getTask());
            current = current.getNext();
        }

        // إحصائيات
        int total = countTasks();
        int completed = countCompletedTasks();
        System.out.println("====================");
        System.out.printf("الإجمالي: %d | المكتملة: %d | المتبقية: %d\n",
                total, completed, total - completed);
    }

    // عرض المهام المكتملة فقط
    public void displayCompletedTasks() {
        System.out.println("\n=== المهام المكتملة ===");
        TaskNode current = head;
        boolean found = false;

        while (current != null) {
            if (current.getTask().isCompleted()) {
                System.out.println(current.getTask());
                found = true;
            }
            current = current.getNext();
        }

        if (!found) {
            System.out.println("لا توجد مهام مكتملة!");
        }
    }

    // عرض المهام غير المكتملة فقط
    public void displayPendingTasks() {
        System.out.println("\n=== المهام المعلقة ===");
        TaskNode current = head;
        boolean found = false;

        while (current != null) {
            if (!current.getTask().isCompleted()) {
                System.out.println(current.getTask());
                found = true;
            }
            current = current.getNext();
        }

        if (!found) {
            System.out.println("لا توجد مهام معلقة!");
        }
    }

    // عد جميع المهام
    private int countTasks() {
        int count = 0;
        TaskNode current = head;
        while (current != null) {
            count++;
            current = current.getNext();
        }
        return count;
    }

    // عد المهام المكتملة
    private int countCompletedTasks() {
        int count = 0;
        TaskNode current = head;
        while (current != null) {
            if (current.getTask().isCompleted()) {
                count++;
            }
            current = current.getNext();
        }
        return count;
    }

    // البحث عن مهام تحتوي على نص معين
    public void searchTasks(String keyword) {
        System.out.println("\n=== نتائج البحث عن: \"" + keyword + "\" ===");
        TaskNode current = head;
        boolean found = false;

        while (current != null) {
            String taskTitle = current.getTask().getTitle().toLowerCase();
            String taskDesc = current.getTask().getDescription().toLowerCase();
            String searchKey = keyword.toLowerCase();

            if (taskTitle.contains(searchKey) || taskDesc.contains(searchKey)) {
                System.out.println(current.getTask());
                found = true;
            }
            current = current.getNext();
        }

        if (!found) {
            System.out.println("لم يتم العثور على مهام تطابق البحث!");
        }
    }
}