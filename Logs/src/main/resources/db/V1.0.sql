toc.dat                                                                                             0000600 0004000 0002000 00000042207 14670413430 0014446 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        PGDMP                        |           Sistema Geo Login    12.20    16.4 8    M           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false         N           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false         O           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false         P           1262    20582    Sistema Geo Login    DATABASE     �   CREATE DATABASE "Sistema Geo Login" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Mexico.1252';
 #   DROP DATABASE "Sistema Geo Login";
                postgres    false                     2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                postgres    false         Q           0    0    SCHEMA public    ACL     Q   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;
                   postgres    false    7         �            1255    24612    Funcion no soportada()    FUNCTION     �   CREATE FUNCTION public."Funcion no soportada"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Begin 
	Raise Exception 'Operación no soportada';
End;$$;
 /   DROP FUNCTION public."Funcion no soportada"();
       public          postgres    false    7         �            1255    24608    prohibido eliminar()    FUNCTION     �   CREATE FUNCTION public."prohibido eliminar"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Begin
	if TG_OP = 'Remove' then
		Raise Exception 'Operación no soportada';
		End if;
		End;$$;
 -   DROP FUNCTION public."prohibido eliminar"();
       public          postgres    false    7         �            1255    24606    prohibido insertar()    FUNCTION     �   CREATE FUNCTION public."prohibido insertar"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Begin
	if TG_OP = 'Insert' then
		Raise Exception 'Operación no soportada';
		End if;
		End;$$;
 -   DROP FUNCTION public."prohibido insertar"();
       public          postgres    false    7         �            1255    24607    prohibido modificar()    FUNCTION     �   CREATE FUNCTION public."prohibido modificar"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Begin
	if TG_OP = 'Update' then
		Raise Exception 'Operación no soportada';
		End if;
		End;$$;
 .   DROP FUNCTION public."prohibido modificar"();
       public          postgres    false    7         �            1255    24610    sexos maximos()    FUNCTION       CREATE FUNCTION public."sexos maximos"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Declare
	cantidad bigint;
Begin
	Select Count(*)
	into cantidad
	From sexos;
	if cantidad = 2 then
		Raise Exception 'Máximo número de sexo admitidos';
	else 
		return NEW;
	End if;
End;$$;
 (   DROP FUNCTION public."sexos maximos"();
       public          postgres    false    7         �            1255    24588    tabla no modificable()    FUNCTION     �   CREATE FUNCTION public."tabla no modificable"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Begin 
	Raise Exception 'Tabla no modificable';
	End;$$;
 /   DROP FUNCTION public."tabla no modificable"();
       public          postgres    false    7         �            1255    24597    usuarios con rol()    FUNCTION     �  CREATE FUNCTION public."usuarios con rol"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Declare
	variable bigint := 0;
Begin
	SELECT COUNT(*)
    INTO variable
    FROM roles
    INNER JOIN usuarios 
	ON roles.rolid = usuarios.rolid and roles.rolid = OLD.rolid;
    IF variable != 0 THEN
        RETURN OLD;
    END IF;
    RAISE EXCEPTION 'Hay usuarios afiliados a dicho rol';
End;$$;
 +   DROP FUNCTION public."usuarios con rol"();
       public          postgres    false    7         �            1259    20583    estados    TABLE     �   CREATE TABLE public.estados (
    estadoid bigint NOT NULL,
    descripcion character varying(100) NOT NULL,
    nombre character varying(100) NOT NULL
);
    DROP TABLE public.estados;
       public         heap    postgres    false    7         �            1259    20586    estados_seq    SEQUENCE     u   CREATE SEQUENCE public.estados_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.estados_seq;
       public          postgres    false    7         �            1259    20588 	   registros    TABLE     -  CREATE TABLE public.registros (
    registroid bigint NOT NULL,
    actividad character varying(100) NOT NULL,
    fecha timestamp(6) without time zone NOT NULL,
    idusuario bigint,
    direccion_ip character varying(15) NOT NULL,
    estadoid bigint NOT NULL,
    mensaje character varying(255)
);
    DROP TABLE public.registros;
       public         heap    postgres    false    7         �            1259    20591    registros_seq    SEQUENCE     w   CREATE SEQUENCE public.registros_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.registros_seq;
       public          postgres    false    7         �            1259    20593    roles    TABLE     h   CREATE TABLE public.roles (
    rolid bigint NOT NULL,
    rol_nombre character varying(50) NOT NULL
);
    DROP TABLE public.roles;
       public         heap    postgres    false    7         �            1259    20596 	   roles_seq    SEQUENCE     s   CREATE SEQUENCE public.roles_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
     DROP SEQUENCE public.roles_seq;
       public          postgres    false    7         �            1259    20598    sexos    TABLE     �   CREATE TABLE public.sexos (
    sexoid bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    sigla character varying(5) NOT NULL
);
    DROP TABLE public.sexos;
       public         heap    postgres    false    7         �            1259    20601 	   sexos_seq    SEQUENCE     s   CREATE SEQUENCE public.sexos_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
     DROP SEQUENCE public.sexos_seq;
       public          postgres    false    7         �            1259    20603    usuarios    TABLE     �  CREATE TABLE public.usuarios (
    usuarioid bigint NOT NULL,
    activo boolean NOT NULL,
    email character varying(100) NOT NULL,
    creacion timestamp(6) without time zone NOT NULL,
    eliminacion timestamp(6) without time zone,
    nombre character varying(100) NOT NULL,
    password character varying(1000) NOT NULL,
    username character varying(50) NOT NULL,
    rolid bigint NOT NULL,
    sexoid bigint NOT NULL
);
    DROP TABLE public.usuarios;
       public         heap    postgres    false    7         �            1259    20609    usuarios_seq    SEQUENCE     v   CREATE SEQUENCE public.usuarios_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.usuarios_seq;
       public          postgres    false    7         A          0    20583    estados 
   TABLE DATA           @   COPY public.estados (estadoid, descripcion, nombre) FROM stdin;
    public          postgres    false    202       2881.dat C          0    20588 	   registros 
   TABLE DATA           m   COPY public.registros (registroid, actividad, fecha, idusuario, direccion_ip, estadoid, mensaje) FROM stdin;
    public          postgres    false    204       2883.dat E          0    20593    roles 
   TABLE DATA           2   COPY public.roles (rolid, rol_nombre) FROM stdin;
    public          postgres    false    206       2885.dat G          0    20598    sexos 
   TABLE DATA           6   COPY public.sexos (sexoid, nombre, sigla) FROM stdin;
    public          postgres    false    208       2887.dat I          0    20603    usuarios 
   TABLE DATA           ~   COPY public.usuarios (usuarioid, activo, email, creacion, eliminacion, nombre, password, username, rolid, sexoid) FROM stdin;
    public          postgres    false    210       2889.dat R           0    0    estados_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.estados_seq', 1651, true);
          public          postgres    false    203         S           0    0    registros_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.registros_seq', 501, true);
          public          postgres    false    205         T           0    0 	   roles_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.roles_seq', 1651, true);
          public          postgres    false    207         U           0    0 	   sexos_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.sexos_seq', 1651, true);
          public          postgres    false    209         V           0    0    usuarios_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.usuarios_seq', 51, true);
          public          postgres    false    211         �
           2606    20612    estados estados_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.estados
    ADD CONSTRAINT estados_pkey PRIMARY KEY (estadoid);
 >   ALTER TABLE ONLY public.estados DROP CONSTRAINT estados_pkey;
       public            postgres    false    202         �
           2606    20614    registros registros_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.registros
    ADD CONSTRAINT registros_pkey PRIMARY KEY (registroid);
 B   ALTER TABLE ONLY public.registros DROP CONSTRAINT registros_pkey;
       public            postgres    false    204         �
           2606    20616    roles roles_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (rolid);
 :   ALTER TABLE ONLY public.roles DROP CONSTRAINT roles_pkey;
       public            postgres    false    206         �
           2606    20618    sexos sexos_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.sexos
    ADD CONSTRAINT sexos_pkey PRIMARY KEY (sexoid);
 :   ALTER TABLE ONLY public.sexos DROP CONSTRAINT sexos_pkey;
       public            postgres    false    208         �
           2606    20620 "   sexos uk_63qfmldbuh3ceq5jh0wmdsh7q 
   CONSTRAINT     ^   ALTER TABLE ONLY public.sexos
    ADD CONSTRAINT uk_63qfmldbuh3ceq5jh0wmdsh7q UNIQUE (sigla);
 L   ALTER TABLE ONLY public.sexos DROP CONSTRAINT uk_63qfmldbuh3ceq5jh0wmdsh7q;
       public            postgres    false    208         �
           2606    20622 $   estados uk_93g5ha066boi1ef1x390la9rc 
   CONSTRAINT     a   ALTER TABLE ONLY public.estados
    ADD CONSTRAINT uk_93g5ha066boi1ef1x390la9rc UNIQUE (nombre);
 N   ALTER TABLE ONLY public.estados DROP CONSTRAINT uk_93g5ha066boi1ef1x390la9rc;
       public            postgres    false    202         �
           2606    20624 %   usuarios uk_io49vjba68pmbgpy9vtw8vm81 
   CONSTRAINT     b   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT uk_io49vjba68pmbgpy9vtw8vm81 UNIQUE (nombre);
 O   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT uk_io49vjba68pmbgpy9vtw8vm81;
       public            postgres    false    210         �
           2606    20626 %   usuarios uk_kfsp0s1tflm1cwlj8idhqsad0 
   CONSTRAINT     a   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT uk_kfsp0s1tflm1cwlj8idhqsad0 UNIQUE (email);
 O   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT uk_kfsp0s1tflm1cwlj8idhqsad0;
       public            postgres    false    210         �
           2606    20628 %   usuarios uk_m2dvbwfge291euvmk6vkkocao 
   CONSTRAINT     d   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT uk_m2dvbwfge291euvmk6vkkocao UNIQUE (username);
 O   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT uk_m2dvbwfge291euvmk6vkkocao;
       public            postgres    false    210         �
           2606    20630 "   roles uk_rn4x84cv8llunnoky04okfkci 
   CONSTRAINT     c   ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uk_rn4x84cv8llunnoky04okfkci UNIQUE (rol_nombre);
 L   ALTER TABLE ONLY public.roles DROP CONSTRAINT uk_rn4x84cv8llunnoky04okfkci;
       public            postgres    false    206         �
           2606    20632 $   estados uk_tc0jqqgxhsvxjns1qcsnet2pq 
   CONSTRAINT     f   ALTER TABLE ONLY public.estados
    ADD CONSTRAINT uk_tc0jqqgxhsvxjns1qcsnet2pq UNIQUE (descripcion);
 N   ALTER TABLE ONLY public.estados DROP CONSTRAINT uk_tc0jqqgxhsvxjns1qcsnet2pq;
       public            postgres    false    202         �
           2606    20634 "   sexos uk_tnbxpr91ldmh7m80x873384gc 
   CONSTRAINT     _   ALTER TABLE ONLY public.sexos
    ADD CONSTRAINT uk_tnbxpr91ldmh7m80x873384gc UNIQUE (nombre);
 L   ALTER TABLE ONLY public.sexos DROP CONSTRAINT uk_tnbxpr91ldmh7m80x873384gc;
       public            postgres    false    208         �
           2606    20636    usuarios usuarios_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (usuarioid);
 @   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT usuarios_pkey;
       public            postgres    false    210         �
           2620    24590    estados Estados no modificable    TRIGGER     �   CREATE TRIGGER "Estados no modificable" BEFORE INSERT OR DELETE OR UPDATE ON public.estados FOR EACH ROW EXECUTE FUNCTION public."tabla no modificable"();
 9   DROP TRIGGER "Estados no modificable" ON public.estados;
       public          postgres    false    212    202         �
           2620    24613    sexos No modificar    TRIGGER     �   CREATE TRIGGER "No modificar" BEFORE DELETE OR UPDATE ON public.sexos FOR EACH ROW EXECUTE FUNCTION public."Funcion no soportada"();
 -   DROP TRIGGER "No modificar" ON public.sexos;
       public          postgres    false    230    208         �
           2620    24615 #   registros Operaciones no permitidas    TRIGGER     �   CREATE TRIGGER "Operaciones no permitidas" BEFORE DELETE OR UPDATE ON public.registros FOR EACH ROW EXECUTE FUNCTION public."Funcion no soportada"();
 >   DROP TRIGGER "Operaciones no permitidas" ON public.registros;
       public          postgres    false    204    230         �
           2620    24598    roles Roles con usuario    TRIGGER     |   CREATE TRIGGER "Roles con usuario" BEFORE DELETE ON public.roles FOR EACH ROW EXECUTE FUNCTION public."usuarios con rol"();
 2   DROP TRIGGER "Roles con usuario" ON public.roles;
       public          postgres    false    213    206         �
           2620    24591    roles Roles no modificables    TRIGGER     �   CREATE TRIGGER "Roles no modificables" BEFORE INSERT OR DELETE OR UPDATE ON public.roles FOR EACH ROW EXECUTE FUNCTION public."tabla no modificable"();
 6   DROP TRIGGER "Roles no modificables" ON public.roles;
       public          postgres    false    206    212         �
           2620    24614    sexos Sexos maximos    TRIGGER     u   CREATE TRIGGER "Sexos maximos" BEFORE INSERT ON public.sexos FOR EACH ROW EXECUTE FUNCTION public."sexos maximos"();
 .   DROP TRIGGER "Sexos maximos" ON public.sexos;
       public          postgres    false    229    208         �
           2620    24616    usuarios usuarios no eliminable    TRIGGER     �   CREATE TRIGGER "usuarios no eliminable" BEFORE DELETE ON public.usuarios FOR EACH ROW EXECUTE FUNCTION public."Funcion no soportada"();
 :   DROP TRIGGER "usuarios no eliminable" ON public.usuarios;
       public          postgres    false    210    230         �
           2606    20637 $   usuarios fk78kw0t13jqp78rnew5ck07iwn    FK CONSTRAINT     �   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT fk78kw0t13jqp78rnew5ck07iwn FOREIGN KEY (sexoid) REFERENCES public.sexos(sexoid);
 N   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT fk78kw0t13jqp78rnew5ck07iwn;
       public          postgres    false    2732    208    210         �
           2606    20642 %   registros fkdi9b8fxeix26dgcjvulepci5u    FK CONSTRAINT     �   ALTER TABLE ONLY public.registros
    ADD CONSTRAINT fkdi9b8fxeix26dgcjvulepci5u FOREIGN KEY (estadoid) REFERENCES public.estados(estadoid);
 O   ALTER TABLE ONLY public.registros DROP CONSTRAINT fkdi9b8fxeix26dgcjvulepci5u;
       public          postgres    false    204    2720    202         �
           2606    20647 $   usuarios fkh0rjch4pwipdc6mj42s4kclns    FK CONSTRAINT     �   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT fkh0rjch4pwipdc6mj42s4kclns FOREIGN KEY (rolid) REFERENCES public.roles(rolid);
 N   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT fkh0rjch4pwipdc6mj42s4kclns;
       public          postgres    false    2728    206    210                                                                                                                                                                                                                                                                                                                                                                                                 2881.dat                                                                                            0000600 0004000 0002000 00000000126 14670413430 0014255 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	La petición a sido aceptada	Aceptado
2	La petición a sido denegada	Rechazado
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                          2883.dat                                                                                            0000600 0004000 0002000 00000003571 14670413430 0014266 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	Insertar usuario: Rayner	2024-09-02 11:10:51.030573	\N	127.0.0.1	1	\N
2	Insertar o reactivar usuario: Dianelis	2024-09-02 11:11:29.654235	\N	127.0.0.1	2	\N
3	Insertar usuario: Dianelis	2024-09-02 11:11:58.340427	\N	127.0.0.1	1	\N
52	Autenticación de usuario: Rayner	2024-09-02 13:14:08.319051	\N	127.0.0.1	1	\N
102	Autenticación de usuario: Rayner	2024-09-03 03:28:59.803184	\N	127.0.0.1	1	\N
103	Obtener listado de todos las provincias del sistema	2024-09-03 03:29:22.520922	1	127.0.0.1	1	\N
104	Listar todos todos los municipios del sistema	2024-09-03 03:30:44.61333	1	127.0.0.1	1	\N
152	Listar todos todos los municipios del sistema	2024-09-03 03:35:04.705633	1	127.0.0.1	1	\N
153	Listar todos todos los municipios del sistema	2024-09-03 03:35:20.535822	1	127.0.0.1	1	\N
154	Obtener el listado de todas las zonas de transportes del sistema	2024-09-03 03:55:10.082563	1	127.0.0.1	2	\N
155	Obtener el listado de todas las zonas de transportes del sistema	2024-09-03 03:59:30.797736	1	127.0.0.1	1	\N
156	Obtener el listado de todas las zonas de transportes del sistema	2024-09-03 04:00:18.927093	1	127.0.0.1	1	\N
202	Autenticación de usuario: Rayner	2024-09-03 11:35:15.498347	\N	127.0.0.1	1	\N
252	Autenticación de usuario: Rayner	2024-09-07 15:11:31.488566	\N	127.0.0.1	2	\N
302	Autenticación de usuario: Rayner	2024-09-07 15:29:47.221721	\N	127.0.0.1	2	\N
352	Autenticación de usuario: Rayner	2024-09-07 15:39:28.692014	\N	127.0.0.1	2	Cannot invoke "cu.edu.cujae.logs.core.mapper.Usuario.getUuid()" because "usuario" is null
402	Autenticación de usuario: Rayner	2024-09-08 12:50:12.604328	\N	127.0.0.1	1	\N
452	Autenticación de usuario: Rayner	2024-09-08 13:01:59.244895	\N	127.0.0.1	1	\N
453	Listar todas las entidades del sistema	2024-09-08 13:06:11.948155	1	127.0.0.1	1	\N
454	Listado de empleados del sistema junto con sus centros laborales	2024-09-08 14:43:47.900762	1	0:0:0:0:0:0:0:1	1	\N
\.


                                                                                                                                       2885.dat                                                                                            0000600 0004000 0002000 00000000064 14670413430 0014262 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	Super Administrador
2	Administrador
3	Gestor
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                            2887.dat                                                                                            0000600 0004000 0002000 00000000040 14670413430 0014256 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	Masculino	M
2	Femenino	F
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                2889.dat                                                                                            0000600 0004000 0002000 00000000524 14670413430 0014267 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	t	raynersoto01@gmail.com	2024-09-02 11:10:50.967448	\N	Rayner Alejandro Soto Martínez	$2a$10$IL3H7xfMROZZuYrvSS.aaeB0BvhHJk5gBs24VfQFyV80EhUG9Ka1i	Rayner	1	1
2	t	dianimerci2001@gmail.com	2024-09-02 11:11:58.328096	\N	Dianelis de las Mercedes Estenoz Vazquez	$2a$10$QLdtdlhmFTrdWe6nvSR2xuiMX39EUeQti/t6Mj52S6LB8N6dR.Mg6	Dianelis	1	2
\.


                                                                                                                                                                            restore.sql                                                                                         0000600 0004000 0002000 00000033567 14670413430 0015404 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        --
-- NOTE:
--
-- File paths need to be edited. Search for $$PATH$$ and
-- replace it with the path to the directory containing
-- the extracted data files.
--
--
-- PostgreSQL database dump
--

-- Dumped from database version 12.20
-- Dumped by pg_dump version 16.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE "Sistema Geo Login";
--
-- Name: Sistema Geo Login; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "Sistema Geo Login" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Mexico.1252';


ALTER DATABASE "Sistema Geo Login" OWNER TO postgres;

\connect -reuse-previous=on "dbname='Sistema Geo Login'"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: Funcion no soportada(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."Funcion no soportada"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Begin 
	Raise Exception 'Operación no soportada';
End;$$;


ALTER FUNCTION public."Funcion no soportada"() OWNER TO postgres;

--
-- Name: prohibido eliminar(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."prohibido eliminar"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Begin
	if TG_OP = 'Remove' then
		Raise Exception 'Operación no soportada';
		End if;
		End;$$;


ALTER FUNCTION public."prohibido eliminar"() OWNER TO postgres;

--
-- Name: prohibido insertar(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."prohibido insertar"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Begin
	if TG_OP = 'Insert' then
		Raise Exception 'Operación no soportada';
		End if;
		End;$$;


ALTER FUNCTION public."prohibido insertar"() OWNER TO postgres;

--
-- Name: prohibido modificar(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."prohibido modificar"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Begin
	if TG_OP = 'Update' then
		Raise Exception 'Operación no soportada';
		End if;
		End;$$;


ALTER FUNCTION public."prohibido modificar"() OWNER TO postgres;

--
-- Name: sexos maximos(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."sexos maximos"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Declare
	cantidad bigint;
Begin
	Select Count(*)
	into cantidad
	From sexos;
	if cantidad = 2 then
		Raise Exception 'Máximo número de sexo admitidos';
	else 
		return NEW;
	End if;
End;$$;


ALTER FUNCTION public."sexos maximos"() OWNER TO postgres;

--
-- Name: tabla no modificable(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."tabla no modificable"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Begin 
	Raise Exception 'Tabla no modificable';
	End;$$;


ALTER FUNCTION public."tabla no modificable"() OWNER TO postgres;

--
-- Name: usuarios con rol(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."usuarios con rol"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$Declare
	variable bigint := 0;
Begin
	SELECT COUNT(*)
    INTO variable
    FROM roles
    INNER JOIN usuarios 
	ON roles.rolid = usuarios.rolid and roles.rolid = OLD.rolid;
    IF variable != 0 THEN
        RETURN OLD;
    END IF;
    RAISE EXCEPTION 'Hay usuarios afiliados a dicho rol';
End;$$;


ALTER FUNCTION public."usuarios con rol"() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: estados; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.estados (
    estadoid bigint NOT NULL,
    descripcion character varying(100) NOT NULL,
    nombre character varying(100) NOT NULL
);


ALTER TABLE public.estados OWNER TO postgres;

--
-- Name: estados_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.estados_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.estados_seq OWNER TO postgres;

--
-- Name: registros; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.registros (
    registroid bigint NOT NULL,
    actividad character varying(100) NOT NULL,
    fecha timestamp(6) without time zone NOT NULL,
    idusuario bigint,
    direccion_ip character varying(15) NOT NULL,
    estadoid bigint NOT NULL,
    mensaje character varying(255)
);


ALTER TABLE public.registros OWNER TO postgres;

--
-- Name: registros_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.registros_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.registros_seq OWNER TO postgres;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    rolid bigint NOT NULL,
    rol_nombre character varying(50) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- Name: roles_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_seq OWNER TO postgres;

--
-- Name: sexos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sexos (
    sexoid bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    sigla character varying(5) NOT NULL
);


ALTER TABLE public.sexos OWNER TO postgres;

--
-- Name: sexos_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sexos_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sexos_seq OWNER TO postgres;

--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios (
    usuarioid bigint NOT NULL,
    activo boolean NOT NULL,
    email character varying(100) NOT NULL,
    creacion timestamp(6) without time zone NOT NULL,
    eliminacion timestamp(6) without time zone,
    nombre character varying(100) NOT NULL,
    password character varying(1000) NOT NULL,
    username character varying(50) NOT NULL,
    rolid bigint NOT NULL,
    sexoid bigint NOT NULL
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- Name: usuarios_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuarios_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuarios_seq OWNER TO postgres;

--
-- Data for Name: estados; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.estados (estadoid, descripcion, nombre) FROM stdin;
\.
COPY public.estados (estadoid, descripcion, nombre) FROM '$$PATH$$/2881.dat';

--
-- Data for Name: registros; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.registros (registroid, actividad, fecha, idusuario, direccion_ip, estadoid, mensaje) FROM stdin;
\.
COPY public.registros (registroid, actividad, fecha, idusuario, direccion_ip, estadoid, mensaje) FROM '$$PATH$$/2883.dat';

--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (rolid, rol_nombre) FROM stdin;
\.
COPY public.roles (rolid, rol_nombre) FROM '$$PATH$$/2885.dat';

--
-- Data for Name: sexos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sexos (sexoid, nombre, sigla) FROM stdin;
\.
COPY public.sexos (sexoid, nombre, sigla) FROM '$$PATH$$/2887.dat';

--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuarios (usuarioid, activo, email, creacion, eliminacion, nombre, password, username, rolid, sexoid) FROM stdin;
\.
COPY public.usuarios (usuarioid, activo, email, creacion, eliminacion, nombre, password, username, rolid, sexoid) FROM '$$PATH$$/2889.dat';

--
-- Name: estados_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.estados_seq', 1651, true);


--
-- Name: registros_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.registros_seq', 501, true);


--
-- Name: roles_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_seq', 1651, true);


--
-- Name: sexos_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sexos_seq', 1651, true);


--
-- Name: usuarios_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuarios_seq', 51, true);


--
-- Name: estados estados_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estados
    ADD CONSTRAINT estados_pkey PRIMARY KEY (estadoid);


--
-- Name: registros registros_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT registros_pkey PRIMARY KEY (registroid);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (rolid);


--
-- Name: sexos sexos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sexos
    ADD CONSTRAINT sexos_pkey PRIMARY KEY (sexoid);


--
-- Name: sexos uk_63qfmldbuh3ceq5jh0wmdsh7q; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sexos
    ADD CONSTRAINT uk_63qfmldbuh3ceq5jh0wmdsh7q UNIQUE (sigla);


--
-- Name: estados uk_93g5ha066boi1ef1x390la9rc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estados
    ADD CONSTRAINT uk_93g5ha066boi1ef1x390la9rc UNIQUE (nombre);


--
-- Name: usuarios uk_io49vjba68pmbgpy9vtw8vm81; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT uk_io49vjba68pmbgpy9vtw8vm81 UNIQUE (nombre);


--
-- Name: usuarios uk_kfsp0s1tflm1cwlj8idhqsad0; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT uk_kfsp0s1tflm1cwlj8idhqsad0 UNIQUE (email);


--
-- Name: usuarios uk_m2dvbwfge291euvmk6vkkocao; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT uk_m2dvbwfge291euvmk6vkkocao UNIQUE (username);


--
-- Name: roles uk_rn4x84cv8llunnoky04okfkci; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uk_rn4x84cv8llunnoky04okfkci UNIQUE (rol_nombre);


--
-- Name: estados uk_tc0jqqgxhsvxjns1qcsnet2pq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estados
    ADD CONSTRAINT uk_tc0jqqgxhsvxjns1qcsnet2pq UNIQUE (descripcion);


--
-- Name: sexos uk_tnbxpr91ldmh7m80x873384gc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sexos
    ADD CONSTRAINT uk_tnbxpr91ldmh7m80x873384gc UNIQUE (nombre);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (usuarioid);


--
-- Name: estados Estados no modificable; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "Estados no modificable" BEFORE INSERT OR DELETE OR UPDATE ON public.estados FOR EACH ROW EXECUTE FUNCTION public."tabla no modificable"();


--
-- Name: sexos No modificar; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "No modificar" BEFORE DELETE OR UPDATE ON public.sexos FOR EACH ROW EXECUTE FUNCTION public."Funcion no soportada"();


--
-- Name: registros Operaciones no permitidas; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "Operaciones no permitidas" BEFORE DELETE OR UPDATE ON public.registros FOR EACH ROW EXECUTE FUNCTION public."Funcion no soportada"();


--
-- Name: roles Roles con usuario; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "Roles con usuario" BEFORE DELETE ON public.roles FOR EACH ROW EXECUTE FUNCTION public."usuarios con rol"();


--
-- Name: roles Roles no modificables; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "Roles no modificables" BEFORE INSERT OR DELETE OR UPDATE ON public.roles FOR EACH ROW EXECUTE FUNCTION public."tabla no modificable"();


--
-- Name: sexos Sexos maximos; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "Sexos maximos" BEFORE INSERT ON public.sexos FOR EACH ROW EXECUTE FUNCTION public."sexos maximos"();


--
-- Name: usuarios usuarios no eliminable; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "usuarios no eliminable" BEFORE DELETE ON public.usuarios FOR EACH ROW EXECUTE FUNCTION public."Funcion no soportada"();


--
-- Name: usuarios fk78kw0t13jqp78rnew5ck07iwn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT fk78kw0t13jqp78rnew5ck07iwn FOREIGN KEY (sexoid) REFERENCES public.sexos(sexoid);


--
-- Name: registros fkdi9b8fxeix26dgcjvulepci5u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT fkdi9b8fxeix26dgcjvulepci5u FOREIGN KEY (estadoid) REFERENCES public.estados(estadoid);


--
-- Name: usuarios fkh0rjch4pwipdc6mj42s4kclns; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT fkh0rjch4pwipdc6mj42s4kclns FOREIGN KEY (rolid) REFERENCES public.roles(rolid);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         