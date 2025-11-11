# Use the latest MySQL image
FROM mysql/mysql-server:latest
# Set the working directory
WORKDIR /tmp
# Copy SQL and dump files
COPY test_db/*.sql /tmp/
COPY test_db/*.dump /tmp/
# Copy initialization SQL script that runs automatically
COPY test_db/employees.sql /docker-entrypoint-initdb.d/
# Add a custom init script to grant remote privileges
COPY init.sql /docker-entrypoint-initdb.d/
# Environment variables
ENV MYSQL_ROOT_PASSWORD=example
ENV MYSQL_DATABASE=employees
