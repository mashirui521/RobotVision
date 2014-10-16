function [sIPAddress, nPort] = mls_supporter_receiveClientAddress(hSupporter)

if nargin < 1
    error('empty argument');
end

if ~isequal(class(hSupporter), ...
    'com.robotvision.javaserver.IServerSupporter')
    error('invalid supporter');
end

hSupporter.receiveClientAddress();
sIPAddress = char(hSupporter.getClientIpAddress());
nPort = hSupporter.getClientPort();

return;
end