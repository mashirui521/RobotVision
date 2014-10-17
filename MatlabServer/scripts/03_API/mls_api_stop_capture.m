function mls_api_stop_capture(hSupporter)
% FUNCTION mls_api_stop_capture(hSupporter)
% Stop capture picture
% 
% --- INPUT
% hSupporter           JavaObject The supporter instance
%
% --- OUTPUT
% 

%% check supporter
if ~isequal(class(hSupporter), ...
    'com.robotvision.javaserver.ServerSupporter')
    error('invalid supporter');
end

%% send stop command
hSupporter.send(com.robotvision.javaserver.utils.Commands.STOP_CAPTURE);


end