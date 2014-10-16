function mls_supporter_send(hSupporter, sOption)
% FUNCTON [sIPAddress, nPort] = mls_supporter_receiveClientAddress(hSupporter)
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
elseif nargin < 2
    error('invalid arguments: option cannot be empty');
end

%% check supporter
if ~isequal(class(hSupporter), ...
    'com.robotvision.javaserver.ServerSupporter')
    error('invalid supporter');
end

%% check option and send command
switch sOption
    case '-login'
        hSupporter.send(com.robotvision.javaserver.utils.Commands.LOGIN);
    otherwise
        error('invalid option: %s', sOption);
end

end