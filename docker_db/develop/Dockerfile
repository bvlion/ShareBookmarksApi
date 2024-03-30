FROM mariadb:10.6
RUN mkdir -p /var/log/mysql
RUN touch /var/log/mysql/mysqld.log

COPY init/*.sql /docker-entrypoint-initdb.d/
COPY develop/init.sql /docker-entrypoint-initdb.d/z_init.sql
