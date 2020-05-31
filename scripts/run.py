#!/usr/bin/python3

import getpass
import os

TITLE = """
.___  ___.   ______   .______      .______    __       _______.
|   \/   |  /  __  \  |   _  \     |   _  \  |  |     /       |
|  \  /  | |  |  |  | |  |_)  |    |  |_)  | |  |    |   (----`
|  |\/|  | |  |  |  | |      /     |   _  <  |  |     \   \    
|  |  |  | |  `--'  | |  |\  \----.|  |_)  | |  | .----)   |   
|__|  |__|  \______/  | _| `._____||______/  |__| |_______/    

"""

WELCOME = """
Welcome to the Morbis Server Setup.
This script will gather basic information needed to intialize the server
and create a runnable file to start the server.

Lets start by creating an admin account:
"""


def setup():
    print(TITLE)
    print(WELCOME)

    admin_username = input("admin username: ")
    admin_password = getpass.getpass("admin password: ")
    admin_name = input("admin name: ")
    admin_email = input("admin email: ")

    print("")

    rep_username = input("association representitive username: ")
    rep_password = getpass.getpass("association representitive password: ")
    rep_name = input("association representitive name: ")
    rep_email = input("association representitive email: ")

    print("")

    print("Next well set up server email\n")
    email_adress = input("server email adress: ")
    email_user = input("server email username: ")
    email_pass = getpass.getpass("server email password: ")

    print("")

    print("Next would you like to connect to a custom database ?\n")
    use_db = input("[yes/no]: ") == "yes"
    db_string = "NA"
    db_user = "NA"
    db_pass=  "NA"
    if use_db:
        db_string = input("database connection string: ")
        db_user = input("database username: ")
        db_pass = getpass.getpass("database password: ")

    print("")

    print("Finally would you like to setup https ?")
    use_ssl = input("[yes/no]: ") == "yes"
    ssl_enabled = "false"
    ssl_store = "NA"
    ssl_pass = "NA"
    ssl_type = "NA"
    ssl_alias = "NA"
    if use_ssl:
        ssl_enabled = "true"
        ssl_store = input("sll key store path: ")
        ssl_pass = getpass.getpass("sll key store password: ")
        ssl_type = input("ssl key store type: ")
        ssl_alias = input("key alias in the store: ")


    with open("application.properties", "w") as props:
        props.write("\n\n")
        props.write("spring.mail.address=" + email_adress + "\n")
        props.write("spring.mail.username=" + email_user + "\n")
        props.write("spring.mail.password=" + email_pass + "\n")
        if use_db:
            props.write("spring.datasource.url=jdbc:" + db_string + "\n")
            props.write("spring.datasource.username=" + db_user + "\n")
            props.write("spring.datasource.password=" + db_pass + "\n")
        if use_ssl:
            props.write("server.ssl.key-store-type=" + ssl_type + "\n")
            props.write("server.ssl.key-store=" + ssl_type + "\n")
            props.write("server.ssl.key-store-password=" + ssl_pass + "\n")
            props.write("server.ssl.key-alias=" + ssl_alias + "\n")
        props.close()

    setup_command = "java -jar morbis.jar --setup"
    setup_admin = " --Ausername=" + admin_username + " --Apassword=" + admin_password + " --Aname=" + admin_name + " --Aemail=" + admin_email
    setup_rep = " --Rusername=" + rep_username + " --Rpassword=" + rep_password + " --Rname=" + rep_name + " --Remail=" + rep_email


    os.system(setup_command + setup_admin + setup_rep)


if __name__ == '__main__':
    if not os.path.isfile("application.properties"):
        setup()
    else:
        os.system("java -jar morbis.jar")


    
        
