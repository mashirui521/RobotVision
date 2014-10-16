function [sIPAddress, nPort] = mls_supporter_receiveClientAddress(hSupporter)
% FUNCTION [sIPAddress, nPort] = mls_supporter_receiveClientAddress(hSupporter)
% Get client address and port
% 
% --- INPUT
% hSupporte         JavaObject  The supporter instance
% 
% --- OUTPUT
% sClientIPAddress  char        The ip address of client
% nPort             int         The port of client
% 

if nargin < 1
    error('empty argument');
end

%% check supporter
if ~isequal(class(hSupporter), ...
    'com.robotvision.javaserver.ServerSupporter')
    error('invalid supporter');
end

%% receive get client address and port
hSupporter.receiveClientAddress();
sIPAddress = char(hSupporter.getClientIpAddress());
nPort = hSupporter.getClientPort();

return;
end