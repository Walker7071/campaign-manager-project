# Campaign Hub Application

Nowoczesna aplikacja **Full-Stack** do zarządzania kampaniami marketingowymi oraz budżetem operacyjnym (**Emerald Wallet**).

---
#  Wersja Live

### Aplikacja została wdrożona w chmurze i jest gotowa do testów: https://campaign-manager-project.vercel.app

**Ważna informacja:** Backend hostowany jest na darmowej platformie Render. Pierwsze żądanie (np. wczytanie salda na start) więcej czasu. Gdy serwer się wybudzi, aplikacja powinna działać płynnie.

# Technologie

## Backend

- Java 21
- Spring Boot 4.1.0
- Spring Data JPA
- Hibernate
- H2 Database (In-Memory)
- Architektura **Package by Feature**

### Struktura domen

```
campaign/
wallet/
```

---

## Frontend

- React
- Vite
- Material UI (MUI)
- Axios

---

# Funkcjonalności

## Emerald Wallet

System posiada konto operacyjne (**Emerald Wallet**), którego saldo jest automatycznie aktualizowane na podstawie wykonywanych operacji na kampaniach.

### Możliwości

- Dynamiczne przeliczanie dostępnego budżetu
- Walidacja uniemożliwiająca utworzenie kampanii przekraczającej dostępne środki
- Automatyczny zwrot środków po usunięciu kampanii
- Aktualizacja salda po zmianie budżetu istniejącej kampanii

---

## Zarządzanie kampaniami (CRUD)

Pełna obsługa cyklu życia kampanii:

-  Tworzenie
-  Odczyt
-  Aktualizacja
-  Usuwanie

Każda operacja wpływająca na budżet automatycznie synchronizuje saldo konta Emerald.

---

## Interfejs użytkownika

Nowoczesny interfejs został zbudowany przy użyciu Material UI.

Najważniejsze elementy:

- Formularz z komponentem **Autocomplete (Typeahead)** dla słów kluczowych
- Responsywna siatka **Grid + Cards**
- Wysuwane boczne menu (**Drawer**) do edycji kampanii
- Czytelny dashboard prezentujący dane

---

# Uruchomienie projektu


## Wymagania

Przed uruchomieniem upewnij się, że masz zainstalowane:

- Java 21
- Node.js
- npm

---

## 1. Uruchomienie Backendu

Backend wykorzystuje bazę danych **H2 In-Memory**, dlatego nie wymaga konfiguracji zewnętrznej bazy danych.

Domyślny port:

```
8080
```

Przejdź do katalogu backendu:

```bash
cd backend
```

Uruchom aplikację:

### Maven

```bash
./mvnw spring-boot:run
```

lub uruchom klasę:

```
CampaignHubApplication
```

z poziomu IntelliJ IDEA.

---

## 2. Uruchomienie Frontendu

Frontend działa na serwerze deweloperskim Vite.

Domyślny port:

```
5173
```

Przejdź do katalogu frontend:

```bash
cd frontend
```

Zainstaluj zależności:

```bash
npm install
```

Uruchom aplikację:

```bash
npm run dev
```

Po uruchomieniu otwórz:

```
http://localhost:5173
```

---

# Notatka 

W kodzie Javy celowo odszedłem od klasycznego układu warstwowego na rzecz podejścia **Package by Feature**. Zamiast dzielić pliki technicznie, podzieliłem je domenowo (na moduł kampanii i portfela). Zależało mi na tym, żeby struktura projektu od razu podpowiadała, co aplikacja robi, a nie tylko z jakich klas się składa.
