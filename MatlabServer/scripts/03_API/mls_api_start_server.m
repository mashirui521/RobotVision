function stInfo = mls_api_start_server(nPort)
% FUNCTION stInfo = mls_api_start_server(nPort)
% Start localhost server
% 
% --- INPUT
% nPort                int     The port number of localhost
% 
% --- OUTPUT
% stInfo            
%   .hSupporter        JavaObject  The supporter instance
%   .sClientIPAddress  char        The ip address of client
%   .nPort             int         The port of client
% 

%% checkout input argument
if nargin < 1
    error('empty argument');
end

stInfo = struct('hSupporter', '', ...
                'sClientIPAddress', '', ...
                'nClientPort', '');

%% initialize supporter
display(sprintf('Start server. IP: %s, Port: &d\n', ...
    char(java.net.Inet4Address.getLocalHost.getHostAddress), nPort));            
stInfo.hSupporter = mls_supporter_get(nPort);

%% listen client response
display('Listening client.......\n');
[stInfo.sClientIPAddress, stInfo.nClientPort] = ...
    mls_supporter_receiveClientAddress(stInfo.hSupporter);
display(sprintf('Get client. Client IP: %s, Port: %d\n', ...
    stInfo.sClientIPAddress, stInfo.nClientPort));

%% send login command
mls_supporter_send(stInfo.hSupporter, '-login');

return;
end