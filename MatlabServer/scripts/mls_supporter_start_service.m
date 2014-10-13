function hService = mls_supporter_start_service(port)

hService = com.robotvision.javaserver.ServerSupporter(port);
hService.startService();
return;
end