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

drop sequence ID_AKCJI_SERWISOWEJ;
drop sequence ID_CZESCI;
drop sequence ID_FAKTURY;
drop sequence ID_KLIENTA;
drop sequence ID_PRACOWNIKA;
drop sequence ID_SAMOCHODU;
drop sequence ID_SERWISU;

create table FAKTURA (
    ID_FAKTURY number primary key,
    KWOTA number(*, 2) not null,
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
    CENA_JEDN number(*, 2) not null
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

create sequence ID_AKCJI_SERWISOWEJ start with 101 increment by 1 nomaxvalue;
create sequence ID_CZESCI start with 201 increment by 1 nomaxvalue;
create sequence ID_FAKTURY start with 301 increment by 1 nomaxvalue;
create sequence ID_KLIENTA start with 1 increment by 1 nomaxvalue;
create sequence ID_PRACOWNIKA start with 501 increment by 1 nomaxvalue;
create sequence ID_SAMOCHODU start with 601 increment by 1 nomaxvalue;
create sequence ID_SERWISU start with 701 increment by 1 nomaxvalue;

create index AKCJA_I on AKCJA_SERWISOWA (ID_SAMOCHODU, ID_KLIENTA, ID_FAKTURY);
create index AKCJA_DATA_I on AKCJA_SERWISOWA (DATA_ROZPOCZECIA);
create index CZESC_NAZWA_I on CZESC (NAZWA);
create index STAN_CZESCI_I on STAN_CZESCI (ID_CZESCI);
create index PRACOWNIK_I on PRACOWNIK (ID_SERWISU);

create or replace function RACHUNEK_KLIENTA(id_klienta in number) return number is
    rachunek number;
    wiersze number;
begin
    select count(*) into wiersze from AKCJA_SERWISOWA where AKCJA_SERWISOWA.id_klienta = id_klienta;
    if wiersze > 0 then
        select sum(kwota) into rachunek
        from AKCJA_SERWISOWA join FAKTURA using (id_faktury)
        where AKCJA_SERWISOWA.id_klienta = id_klienta and (data_zakonczenia is null or data_zakonczenia > sysdate);
        
        return rachunek;
    else 
        return 0;
    end if;
end RACHUNEK_KLIENTA;
/
create or replace procedure WSTAW_KLIENTA(rodzaj varchar2, imie_nazwa varchar2, nazwisko_nip varchar2, adres varchar2) is
    gen_id_klienta number;                                         
begin
    if rodzaj = 'IND' then 
        gen_id_klienta := ID_KLIENTA.NEXTVAL;
        insert into KLIENT values (gen_id_klienta, adres);
        insert into KLIENT_INDYWIDUALNY values (gen_id_klienta, imie_nazwa, nazwisko_nip);
    elsif rodzaj = 'FIRMA' then
        gen_id_klienta := ID_KLIENTA.NEXTVAL;
        insert into KLIENT values (gen_id_klienta, adres);
        insert into FIRMA values (gen_id_klienta, imie_nazwa, nazwisko_nip);
    end if;
end WSTAW_KLIENTA;
/
create or replace procedure WSTAW_SAMOCHOD(nr_rej varchar2, vin varchar2, 
            marka varchar2 default null, 
            model varchar2 default null, 
            pojemnosc number default null, 
            rok_produkcji number default null, 
            rodzaj varchar2 default null) is
    gen_id_samochodu number;                                         
begin
    gen_id_samochodu := ID_SAMOCHODU.NEXTVAL;
    insert into SAMOCHOD values (gen_id_samochodu, nr_rej, vin, marka, model, pojemnosc, rok_produkcji);
    if rodzaj = 'ZASTEPCZY' then 
        insert into SAMOCHOD_ZASTEPCZY values (gen_id_samochodu, 'N');
    end if;
end WSTAW_SAMOCHOD;
/
create or replace procedure WSTAW_CZESC(nazwa varchar2, cena_jedn number) is
    gen_id_czesci number;                                         
begin
    gen_id_czesci := ID_CZESCI.NEXTVAL;
    insert into CZESC values (gen_id_czesci, nazwa, cena_jedn);
end WSTAW_CZESC;
/
create or replace procedure WSTAW_FAKTURE(kwota varchar2, termin_platnosci varchar2 default NULL) is
    gen_id_faktury number;                                         
begin
    gen_id_faktury := ID_FAKTURY.NEXTVAL;
    if termin_platnosci is null then
        insert into FAKTURA values (gen_id_faktury, kwota, SYSDATE+14);
    else 
        insert into FAKTURA values (gen_id_faktury, kwota, TO_DATE(termin_platnosci, 'DD-MM-YYYY'));
        end if;
end WSTAW_FAKTURE;
/
create or replace procedure WSTAW_SERWIS(nazwa varchar2, adres varchar2) is
    gen_id_serwisu number;                                         
begin
    gen_id_serwisu := ID_SERWISU.NEXTVAL;
    insert into SERWIS values (gen_id_serwisu, nazwa, adres);
end WSTAW_SERWIS;
/
create or replace procedure WSTAW_PRACOWNIKA(imie varchar2, nazwisko varchar2, id_serwisu number default null, nazwa_serwisu varchar2 default null) is
    gen_id_pracownika number;   
    id_ser number;
    czy_istnieje number;
begin
    if id_serwisu is null and nazwa_serwisu is not null then
        select COUNT(id_serwisu) into czy_istnieje from SERWIS where nazwa = nazwa_serwisu;
        if czy_istnieje > 0 then
            select id_serwisu into id_ser from SERWIS where nazwa = nazwa_serwisu;
            gen_id_pracownika := ID_PRACOWNIKA.NEXTVAL;
            insert into PRACOWNIK values (gen_id_pracownika, imie, nazwisko, id_ser);
        end if;
    elsif id_serwisu is not null then
        gen_id_pracownika := ID_PRACOWNIKA.NEXTVAL;
        insert into PRACOWNIK values (gen_id_pracownika, imie, nazwisko, id_serwisu);
    end if;
end WSTAW_PRACOWNIKA;
/
create or replace procedure DODAJ_CZESC(id_serwisu number default null, nazwa_serwisu varchar2 default null, id_czesci number default null, nazwa_czesci varchar2 default null, liczba number) is
    id_ser number := null;
    id_cz number := null;
    czy_istnieje_serwis number;
    czy_istnieje_czesc number;
    czy_istnieje_stan number;
begin
    if id_serwisu is null and nazwa_serwisu is not null then
        select COUNT(id_serwisu) into czy_istnieje_serwis from SERWIS where nazwa = nazwa_serwisu;
        if czy_istnieje_serwis > 0 then
            select id_serwisu into id_ser from SERWIS where nazwa = nazwa_serwisu;
        end if;
    elsif id_serwisu is not null then
        id_ser := id_serwisu;
    end if;
    
    if id_czesci is null and nazwa_czesci is not null then
        select COUNT(id_czesci) into czy_istnieje_czesc from CZESC where nazwa = nazwa_czesci;
        if czy_istnieje_czesc > 0 then
            select id_czesci into id_cz from CZESC where nazwa = nazwa_czesci;
        end if;
    elsif id_czesci is not null then
        id_cz := id_czesci;
    end if;
    
    if id_ser is not null and id_cz is not null then
        select count(*) into czy_istnieje_stan from STAN_CZESCI where id_serwisu = id_ser and id_czesci = id_cz;
        
        if czy_istnieje_stan = 1 then
            update STAN_CZESCI set ilosc = ilosc + liczba where STAN_CZESCI.id_serwisu = id_ser and id_czesci = id_cz;
        elsif czy_istnieje_stan = 0 then
            insert into STAN_CZESCI values (id_ser, id_cz, liczba);
        end if;
    end if;
end DODAJ_CZESC;
/
create or replace procedure DODAJ_AKCJE_SERWISOWA(id_samochodu number, 
                                                  id_klienta number, 
                                                  data_rozpoczecia varchar2 default to_char(sysdate, 'DD-MM-YYYY'), 
                                                  opis varchar2 default null, 
                                                  kwota number default null, 
                                                  termin_platnosci varchar2 default null, 
                                                  id_pracownika number default null, 
                                                  id_samochodu_zastepczego number default null, 
                                                  data_zakonczenia varchar2 default null) is
    gen_id_akcji number;
    gen_id_faktury number;
begin
    gen_id_akcji := ID_AKCJI_SERWISOWEJ.NEXTVAL;
    if kwota is not null then 
        gen_id_faktury := ID_FAKTURY.NEXTVAL;
        WSTAW_FAKTURE(kwota, termin_platnosci, gen_id_faktury);
    end if;
    
    insert into AKCJA_SERWISOWA values (gen_id_akcji, 
                                        id_samochodu, 
                                        id_klienta, 
                                        opis, 
                                        gen_id_faktury, 
                                        id_pracownika, 
                                        id_samochodu_zastepczego, 
                                        to_date(data_rozpoczecia, 'DD-MM-YYYY'),
                                        to_date(data_zakonczenia, 'DD-MM-YYYY'));
end DODAJ_AKCJE_SERWISOWA;
/
execute WSTAW_KLIENTA('IND', 'Jan', 'Kowalski', 'Glicynii 48, 04-855 Warszawa');
execute WSTAW_KLIENTA('IND', 'Adam', 'Nowak', 'Chmielna 105, 51-212 Wrocław');
execute WSTAW_KLIENTA('IND', 'Gustaw', 'Dąbrowski', 'Świechockiego 139, 80-041 Gdańsk');
execute WSTAW_KLIENTA('IND', 'Michał', 'Jaworski', 'Akacjowa 93, 41-712 Ruda Śląska');
execute WSTAW_KLIENTA('IND', 'Ewa', 'Jabłońska', 'Morelowa 72, 85-362 Bydgoszcz');
execute WSTAW_KLIENTA('IND', 'Benedykt', 'Kwiatkowski', 'Gołębia 86, 03-662 Warszawa');
execute WSTAW_KLIENTA('FIRMA', 'Politechnika Poznańska', '7770003699', 'pl. Marii Skłodowskiej-Curie 5, Poznań');
execute WSTAW_KLIENTA('FIRMA', 'McBud', '8259953712', 'Elżbiety 5, 47-100 Racibórz');

execute WSTAW_SAMOCHOD('CTR9PJ9', 'WP0ZZZ99ZTS392124', 'Seat', 'Leon', 1197, 2014);
execute WSTAW_SAMOCHOD('CTR9WP6', '1FTFW1E60EFD45173', 'Porsche', 'Cayman', 3392, 2013);
execute WSTAW_SAMOCHOD('CNAAU13', 'KLAJC52ZX11667934', 'Skoda', 'Octavia', 2100, 2005, 'ZASTEPCZY');
execute WSTAW_SAMOCHOD('CTE2304', '3P3XA46K6ST559926', marka => 'Opel', model => 'Astra', pojemnosc => 1920, rok_produkcji => 2001);
execute WSTAW_SAMOCHOD('GDY426E', '1FMDU73W83UA86147', marka => 'Opel', model => 'Vectra', pojemnosc => 1875, rodzaj => 'ZASTEPCZY');

execute WSTAW_CZESC('Sprzęgło', '650');
execute WSTAW_CZESC('Klocki hamulcowe', '150');
execute WSTAW_CZESC('Rozrusznik', '710');
execute WSTAW_CZESC('Przedni reflektor', '435');
execute WSTAW_CZESC('Linka hamulca ręcznego', '260');

execute WSTAW_FAKTURE('1206.23');
execute WSTAW_FAKTURE('945.00', '12-12-2018');
execute WSTAW_FAKTURE('560.00', '13-04-2018');
execute WSTAW_FAKTURE('153.50', '25-11-2018');
execute WSTAW_FAKTURE('455.80', '12-05-2017');

execute WSTAW_SERWIS('Warsztat samochodowy JANSEB', 'Chłopska 16, 10-856 Olsztyn');
execute WSTAW_SERWIS('Warsztat samochodowy MACTECH', 'Podbiałowa 125, 61-680 Poznań');
execute WSTAW_SERWIS('Warsztat samochodowy AUTOWAR', 'Jaremy Marii 5, 31-318 Kraków');
execute WSTAW_SERWIS('Warsztat SAMTECH', 'Opłotki 40, 80-730 Gdańsk');

execute WSTAW_PRACOWNIKA('Krystian', 'Kamiński', 701);
execute WSTAW_PRACOWNIKA('Konstantyn', 'Dąbrowski', 701);
execute WSTAW_PRACOWNIKA('Korneli', 'Michalski', 702);
execute WSTAW_PRACOWNIKA('Albin', 'Dudek', 702);
execute WSTAW_PRACOWNIKA('Ziemowit', 'Jabłoński', nazwa_serwisu => 'Warsztat samochodowy AUTOWAR');
execute WSTAW_PRACOWNIKA('Alojzy', 'Tomaszewski', nazwa_serwisu => 'Warsztat samochodowy AUTOWAR');
execute WSTAW_PRACOWNIKA('Jakub', 'Woźniak', 704);
execute WSTAW_PRACOWNIKA('Miłosz', 'Dudek', nazwa_serwisu => 'Warsztat SAMTECH');

execute DODAJ_CZESC(id_serwisu => 701, id_czesci => 201, liczba => 2);
execute DODAJ_CZESC(id_serwisu => 701, id_czesci => 202, liczba => 4);
execute DODAJ_CZESC(id_serwisu => 701, id_czesci => 203, liczba => 2);
execute DODAJ_CZESC(id_serwisu => 701, nazwa_czesci => 'Przedni reflektor', liczba => 7);
execute DODAJ_CZESC(nazwa_serwisu => 'Warsztat samochodowy JANSEB', id_czesci => 205, liczba => 4);
execute DODAJ_CZESC(id_serwisu => 702, id_czesci => 202, liczba => 1);
execute DODAJ_CZESC(nazwa_serwisu => 'Warsztat samochodowy MACTECH', nazwa_czesci => 'Rozrusznik', liczba => 3);
execute DODAJ_CZESC(id_serwisu => 702, id_czesci => 204, liczba => 2);
execute DODAJ_CZESC(id_serwisu => 703, id_czesci => 204, liczba => 6);
execute DODAJ_CZESC(id_serwisu => 703, id_czesci => 205, liczba => 5);
execute DODAJ_CZESC(id_serwisu => 704, id_czesci => 201, liczba => 7);
execute DODAJ_CZESC(id_serwisu => 704, id_czesci => 205, liczba => 9);

execute DODAJ_AKCJE_SERWISOWA(602, 2, '27-12-2018', 'Wymiana oleju', 150, '13-12-2018', 501, 603, data_zakonczenia => '01-01-2019');

commit;

--SELECT * FROM ALL_OBJECTS WHERE OBJECT_NAME IN ('WSTAW_KLIENTA');
