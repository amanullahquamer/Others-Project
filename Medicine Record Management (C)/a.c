#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_NAME 50
#define FILENAME "medicine.txt"

typedef struct Medicine {
    int id;
    char name[MAX_NAME];
    char manufacturer[MAX_NAME];
    int quantity;
    float price;
    char expiry[11];  // YYYY-MM-DD
    struct Medicine *next;
} Medicine;

// Function prototypes
int login();
void loadMedicines(Medicine **head);
void saveMedicines(Medicine *head);
Medicine* createNode(int id, const char *name, const char *manu, int qty, float price, const char *exp);
void addMedicine(Medicine **head);
void viewMedicines(Medicine *head);
void sortMedicines(Medicine *head);
void searchMedicine(Medicine *head);
void updateMedicine(Medicine *head);
void deleteMedicine(Medicine **head);
void alertExpiry(Medicine *head);
void pauseConsole();

int main() {
    if (!login()) return 0;

    Medicine *head = NULL;
    loadMedicines(&head);
    alertExpiry(head);

    int choice;
    do {
        printf("\n=== Medicine Record Management ===\n");
        printf("1. Add Medicine\n");
        printf("2. View Medicines\n");
        printf("3. Search Medicine\n");
        printf("4. Update Medicine\n");
        printf("5. Delete Medicine\n");
        printf("6. Save & Exit\n");
        printf("Enter choice: ");
        scanf("%d", &choice);
        switch (choice) {
            case 1: addMedicine(&head); break;
            case 2: viewMedicines(head); break;
            case 3: searchMedicine(head); break;
            case 4: updateMedicine(head); break;
            case 5: deleteMedicine(&head); break;
            case 6: saveMedicines(head); printf("Data saved. Exiting...\n"); break;
            default: printf("Invalid choice.\n");
        }
    } while (choice != 6);

    // Free linked list
    Medicine *cur = head;
    while (cur) {
        Medicine *tmp = cur;
        cur = cur->next;
        free(tmp);
    }
    return 0;
}

int login() {
    char user[20], pass[20];
    const char adminUser[] = "admin";
    const char adminPass[] = "admin";
    printf("Username: "); scanf("%19s", user);
    printf("Password: "); scanf("%19s", pass);
    if (strcmp(user, adminUser) == 0 && strcmp(pass, adminPass) == 0) {
        printf("Login successful!\n");
        return 1;
    }
    printf("Invalid credentials.\n");
    return 0;
}

void loadMedicines(Medicine **head) {
    FILE *fp = fopen(FILENAME, "r");
    if (!fp) return;
    char line[256];
    while (fgets(line, sizeof(line), fp)) {
        int id, qty;
        float price;
        char name[MAX_NAME], manu[MAX_NAME], exp[11];
        char *tok = strtok(line, "|\n");
        if (!tok) continue; id = atoi(tok);
        tok = strtok(NULL, "|\n"); if (!tok) continue; strncpy(name, tok, MAX_NAME);
        tok = strtok(NULL, "|\n"); if (!tok) continue; strncpy(manu, tok, MAX_NAME);
        tok = strtok(NULL, "|\n"); if (!tok) continue; qty = atoi(tok);
        tok = strtok(NULL, "|\n"); if (!tok) continue; price = atof(tok);
        tok = strtok(NULL, "|\n"); if (!tok) continue; strncpy(exp, tok, 11);
        Medicine *node = createNode(id, name, manu, qty, price, exp);
        node->next = *head;
        *head = node;
    }
    fclose(fp);
}

void saveMedicines(Medicine *head) {
    FILE *fp = fopen(FILENAME, "w");
    for (Medicine *p = head; p; p = p->next) {
        fprintf(fp, "%d|%s|%s|%d|%.2f|%s\n",
                p->id, p->name, p->manufacturer, p->quantity, p->price, p->expiry);
    }
    fclose(fp);
}

Medicine* createNode(int id, const char *name, const char *manu, int qty, float price, const char *exp) {
    Medicine *m = (Medicine*)malloc(sizeof(Medicine));
    m->id = id;
    strncpy(m->name, name, MAX_NAME);
    strncpy(m->manufacturer, manu, MAX_NAME);
    m->quantity = qty;
    m->price = price;
    strncpy(m->expiry, exp, 11);
    m->next = NULL;
    return m;
}

void addMedicine(Medicine **head) {
    int id, qty;
    float price;
    char name[MAX_NAME], manu[MAX_NAME], exp[11];
    printf("Enter ID: "); scanf("%d", &id);
    printf("Enter Name: "); scanf(" %49[^"]", name);
    printf("Enter Manufacturer: "); scanf(" %49[^"]", manu);
    printf("Enter Quantity: "); scanf("%d", &qty);
    printf("Enter Price: "); scanf("%f", &price);
    printf("Enter Expiry Date (YYYY-MM-DD): "); scanf("%10s", exp);
    Medicine *node = createNode(id, name, manu, qty, price, exp);
    node->next = *head;
    *head = node;
    printf("Medicine added.\n");
}

void viewMedicines(Medicine *head) {
    if (!head) { printf("No records.\n"); return; }
    sortMedicines(head);
    printf("\n%-5s %-20s %-20s %-8s %-10s %-12s\n", "ID", "Name", "Manufacturer", "Qty", "Price", "Expiry");
    printf("---------------------------------------------------------------------\n");
    for (Medicine *p = head; p; p = p->next) {
        printf("%-5d %-20s %-20s %-8d %-10.2f %-12s\n",
               p->id, p->name, p->manufacturer, p->quantity, p->price, p->expiry);
    }
}

void sortMedicines(Medicine *head) {
    if (!head) return;
    int swapped;
    Medicine *ptr1;
    Medicine *lptr = NULL;
    do {
        swapped = 0;
        ptr1 = head;
        while (ptr1->next != lptr) {
            if (ptr1->id > ptr1->next->id) {
                // swap data
                Medicine tmp = *ptr1;
                *ptr1 = *ptr1->next;
                *ptr1->next = tmp.next;
                ptr1->next = ptr1->next->next;
                *ptr1->next = tmp;
                swapped = 1;
            }
            ptr1 = ptr1->next;
        }
        lptr = ptr1;
    } while (swapped);
}

void searchMedicine(Medicine *head) {
    if (!head) { printf("No records.\n"); return; }
    int id; printf("Enter ID to search: "); scanf("%d", &id);
    for (Medicine *p = head; p; p = p->next) {
        if (p->id == id) {
            printf("Found: ID=%d, Name=%s, Manu=%s, Qty=%d, Price=%.2f, Expiry=%s\n",
                   p->id, p->name, p->manufacturer, p->quantity, p->price, p->expiry);
            return;
        }
    }
    printf("Record not found.\n");
}

void updateMedicine(Medicine *head) {
    if (!head) { printf("No records.\n"); return; }
    int id; printf("Enter ID to update: "); scanf("%d", &id);
    for (Medicine *p = head; p; p = p->next) {
        if (p->id == id) {
            printf("Enter new Name: "); scanf(" %49[^"]", p->name);
            printf("Enter new Manufacturer: "); scanf(" %49[^"]", p->manufacturer);
            printf("Enter new Quantity: "); scanf("%d", &p->quantity);
            printf("Enter new Price: "); scanf("%f", &p->price);
            printf("Enter new Expiry Date (YYYY-MM-DD): "); scanf("%10s", p->expiry);
            printf("Record updated.\n");
            return;
        }
    }
    printf("Record not found.\n");
}

void deleteMedicine(Medicine **head) {
    if (!*head) { printf("No records.\n"); return; }
    int id; printf("Enter ID to delete: "); scanf("%d", &id);
    Medicine *cur = *head, *prev = NULL;
    while (cur && cur->id != id) {
        prev = cur;
        cur = cur->next;
    }
    if (!cur) {
        printf("Record not found.\n"); return;
    }
    if (!prev) *head = cur->next;
    else prev->next = cur->next;
    free(cur);
    printf("Record deleted.\n");
}

void alertExpiry(Medicine *head) {
    time_t now = time(NULL);
    struct tm tm_exp;
    char msg[100];
    printf("\n=== Expiry Alerts ===\n");
    for (Medicine *p = head; p; p = p->next) {
        int y, m, d;
        if (sscanf(p->expiry, "%d-%d-%d", &y, &m, &d) != 3) continue;
        tm_exp = *localtime(&now);
        tm_exp.tm_year = y - 1900;
        tm_exp.tm_mon = m - 1;
        tm_exp.tm_mday = d;
        tm_exp.tm_hour = tm_exp.tm_min = tm_exp.tm_sec = 0;
        time_t exp_t = mktime(&tm_exp);
        double days = difftime(exp_t, now) / (60 * 60 * 24);
        if (days < 0) sprintf(msg, "EXPIRED %s!", p->name);
        else if (days <= 30) sprintf(msg, "About to expire %s in %.0f days", p->name, days);
        else continue;
        printf("%s\n", msg);
    }
}

void pauseConsole() {
    printf("Press Enter to continue...");
    getchar(); getchar();
}

