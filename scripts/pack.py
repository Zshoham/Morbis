#!/usr/bin/python3

import os
import subprocess
import shutil
import argparse
import platform


parser = argparse.ArgumentParser(description='Package Morbis for release')
parser.add_argument('-f', '--frontend', action='store_true', help="package the webapp")
parser.add_argument('-b', '--backend', action='store_true', help="package the server")
parser.add_argument('-a', '--all', action='store_true', help="package all")
parser.add_argument('-c', '--clean', action='store_true', help="clean build files")

def pack_front():
    os.chdir("webapp")
    
    if platform.system() == "Windows":
        subprocess.run(["npm.cmd", "run", "build"])
    else:
        subprocess.run(["npm", "run", "build"])

    os.chdir("..")
    shutil.rmtree("src/main/resources/static", ignore_errors=True)
    shutil.move("webapp/dist", "src/main/resources/static")

def pack_back():
    if platform.system() == "Windows":
        subprocess.run(["gradlew.bat", "clean"])
        subprocess.run(["gradlew.bat", "bootjar"])
    else:
        subprocess.run(["./gradlew", "clean"])
        subprocess.run(["./gradlew", "bootjar"])
    
    os.makedirs("release", exist_ok=True)
    jar = os.listdir("build/libs")[0]
    shutil.copy("build/libs/" + jar, "release/morbis.jar")
    shutil.copy("scripts/run.py", "release")

def clean():
    if platform.system() == "Windows":
        subprocess.run(["gradlew.bat", "clean"])
    else:
        subprocess.run(["./gradlew", "clean"])

    shutil.rmtree("release", ignore_errors=True)

if __name__ == '__main__':
    args = parser.parse_args()

    if args.clean:
        clean()

    if args.all:
        pack_front()
        pack_back()
        exit(0)
    
    if args.frontend:
        pack_front()

    if args.backend:
        pack_back()