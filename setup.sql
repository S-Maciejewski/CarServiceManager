host echo "Tworzenie relacji"

create table FAKTURA (
    ID_FAKTURY int primary key,
    KWOTA number(2,8) not null,
    TERMIN_PLATNOSCI date
);

create table SERWIS (
    ID_SERWISU int primary key,
    NAZWA varchar(100) not null,
    ADRES varchar(100) not null
);

create table CZESCI (
    ID_CZESCI int primary key,
    NAZWA varchar(100) not null,
    CENA_JEDN number(2,8) not null
);

create table STAN_CZESCI (
    ID_SERWISU int references SERWIS(ID_SERWISU),
    ID_CZESCI int references CZESCI(ID_CZESCI),
    ILOSC number(2,8) not null
);

create table PRACOWNIK (
    ID_PRACOWNIKA int primary key,
    IMIE varchar(20) not null,
    NAZWISKO varchar(20) not null,
    ID_SERWISU int references SERWIS(ID_SERWISU) 
);

create table AKCJA_SERWISOWA (
    ID_AKCJI int primary key,
    ID_SAMOCHODU int not null,
    ID_KLIENTA int not null,
    OPIS varchar(2048),
    ID_FAKTURY int references FAKTURA(ID_FAKTURY),
    ID_PRACOWNIKA int references PRACOWNIK(ID_PRACOWNIKA),
    ID_SAMOCHODU_ZASTEPCZEGO int,
    DATA_ROZPOCZECIA date not null,
    DATA_ZAKONCZENIA date
);

