function mls_supporter_send(hSupporter, sOption)

if nargin < 1
    error('empty argument');
elseif nargin < 2
    error('invalid arguments: option cannot be empty');
end

if ~isequal(class(hSupporter), ...
    'com.robotvision.javaserver.IServerSupporter')
    error('invalid supporter');
end

switch sOption
    case '-login'
        hSupporter.send(com.robotvision.javaserver.utils.Commands.LOGIN);
    otherwise
        error('invalid option: %s', sOption);
end

end