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

drop sequence ID_SEQ;

drop index PRACOWNIK_I; 
drop index AKCJA_I;
drop index AKCJA_DATA_I;
drop index CZESC_NAZWA_I;
drop index STAN_CZESCI_I;

create table FAKTURA (
    ID_FAKTURY number primary key,
    KWOTA number not null,
    TERMIN_PLATNOSCI date
);

create table SERWIS (
    ID_SERWISU number primary key,
    NAZWA varchar(100) not null,
    ADRES varchar(100) not null
);

create table CZESC (
    ID_CZESCI number primary key,
    NAZWA varchar(100) not null,
    CENA_JEDN number not null
);

create table STAN_CZESCI (
    ID_SERWISU number references SERWIS(ID_SERWISU),
    ID_CZESCI number references CZESC(ID_CZESCI),
    ILOSC number not null
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
    POJEMNOSC number,
    ROK_PRODUKCJI number
);

create table SAMOCHOD_ZASTEPCZY (
    ID_SAMOCHODU_ZASTEPCZEGO number primary key,
    CZY_WYPOZYCZONY char(3), -- 'TAK' albo 'NIE'
    constraint SAMOCHOD_ZASTEPCZY_FK foreign key (ID_SAMOCHODU_ZASTEPCZEGO) references SAMOCHOD(ID_SAMOCHODU)
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

create sequence ID_SEQ start with 0 increment by 1 nomaxvalue minvalue 0;

create index AKCJA_I on AKCJA_SERWISOWA (ID_SAMOCHODU, ID_KLIENTA, ID_FAKTURY);
create index AKCJA_DATA_I on AKCJA_SERWISOWA (DATA_ROZPOCZECIA);
create index CZESC_NAZWA_I on CZESC (NAZWA);
create index STAN_CZESCI_I on STAN_CZESCI (ID_CZESCI);
create index PRACOWNIK_I on PRACOWNIK (ID_SERWISU);


-- Przykładowe dane

insert into SAMOCHOD values (ID_SEQ.nextval, 'CTR9PJ9', 'WP0ZZZ99ZTS392124', 'Seat', 'Leon', 1197, 2014);
insert into SAMOCHOD values (ID_SEQ.nextval, 'CTR9WP6', 'WP0ZZZ62ZTS192398', 'Porsche', 'Cayman', 3392, 2013);

insert into KLIENT values (ID_SEQ.nextval, 'Bankowa 12, Lubicz Górny');
insert into KLIENT_INDYWIDUALNY values (ID_SEQ.currval, 'Sebastian', 'Maciejewski');

insert into AKCJA_SERWISOWA values (ID_SEQ.nextval, 1, 4, 'Wymiana świec zapłonowych', null, null, null, (select sysdate from dual), null);

select * from AKCJA_SERWISOWA;
