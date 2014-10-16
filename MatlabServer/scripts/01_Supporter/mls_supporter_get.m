function hSupporter = mls_supporter_get(port)
% FUNCTION hSupporter = mls_supporter_get(port))
% Get supporter instance
% 
% --- INPUT
% nPort         int     The port number of localhost
% 

hSupporter = com.robotvision.javaserver.ServerSupporter.getInstance(port);
return;
end