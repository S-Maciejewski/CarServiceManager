host echo "Tworzenie relacji"

drop table AKCJA_SERWISOWA;
drop table KLIENT_INDYWIDUALNY;
drop table FIRMA;
drop table KLIENT;
drop table PRACOWNIK;
drop table STAN_CZESCI;
drop table CZESC;
drop table SERWIS;
drop table SAMOCHOD_ZASTEPCZY;
drop table SAMOCHOD;
drop table FAKTURA;

create sequence ID_SEQ start with 0 increment by 1 nomaxvalue minvalue 0;

create table FAKTURA (
    ID_FAKTURY number default ID_SEQ.nextval,
    KWOTA number(2,8) not null,
    TERMIN_PLATNOSCI date,
    constranumber FAKTURA_PK primary key(ID_FAKTURY)
);

create table SERWIS (
    ID_SERWISU number primary key,
    NAZWA varchar(100) not null,
    ADRES varchar(100) not null
);

create table CZESC (
    ID_CZESCI number primary key,
    NAZWA varchar(100) not null,
    CENA_JEDN number(2,8) not null
);

create table STAN_CZESCI (
    ID_SERWISU number references SERWIS(ID_SERWISU),
    ID_CZESCI number references CZESC(ID_CZESCI),
    ILOSC number(2,8) not null
);

create table PRACOWNIK (
    ID_PRACOWNIKA number primary key,
    IMIE varchar(20) not null,
    NAZWISKO varchar(40) not null,
    ID_SERWISU number references SERWIS(ID_SERWISU) 
);

create table SAMOCHOD (
    ID_SAMOCHODU number primary key,
    NUMER_REJESTRACYJNY varchar(8) not null,
    VIN varchar(17) not null,
    MARKA varchar(100),
    MODEL varchar(100),
    POJEMNOSC number(1,5)
);

create table SAMOCHOD_ZASTEPCZY (
    ID_SAMOCHODU_ZASTEPCZEGO number primary key,
    CZY_WYPOZYCZONY char(3), -- 'TAK' albo 'NIE'
    constranumber SAMOCHOD_ZASTEPCZY_FK foreign key (ID_SAMOCHODU_ZASTEPCZEGO) references SAMOCHOD(ID_SAMOCHODU)
);

create table KLIENT (
    ID_KLIENTA number primary key,
    ADRES varchar(100) not null
);

create table KLIENT_INDYWIDUALNY (
    ID_KLIENTA number references KLIENT(ID_KLIENTA),
    IMIE varchar(20) not null,
    NAZWISKO varchar(40) not null
);

create table FIRMA (
    ID_KLIENTA number references KLIENT(ID_KLIENTA),
    NAZWA varchar(100) not null,
    NIP varchar(10) not null
);

create table AKCJA_SERWISOWA (
    ID_AKCJI number primary key,
    ID_SAMOCHODU number not null,
    ID_KLIENTA number not null,
    OPIS varchar(2048),
    ID_FAKTURY number references FAKTURA(ID_FAKTURY),
    ID_PRACOWNIKA number references PRACOWNIK(ID_PRACOWNIKA),
    ID_SAMOCHODU_ZASTEPCZEGO number references SAMOCHOD_ZASTEPCZY(ID_SAMOCHODU_ZASTEPCZEGO),
    DATA_ROZPOCZECIA date not null,
    DATA_ZAKONCZENIA date
);

