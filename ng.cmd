@echo off

setlocal

set PATH=%~dp0node/;%~dp0;%PATH%

node node_modules/@angular/cli/bin/ng %*

@echo on