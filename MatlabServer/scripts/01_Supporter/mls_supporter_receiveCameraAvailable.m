function bAvailable = mls_supporter_receiveCameraAvailable(hSupporter)
% FUNCTION bAvailable = mls_receiveCameraAvailable(hSupporter)
% Check if camera is available
% 
% --- INPUT
% hSupporte         JavaObject  The supporter instance
% 
% --- OUTPUT
% bAvailable        boolean     The camera availability
% 

%% check supporter
if ~isequal(class(hSupporter), ...
    'com.robotvision.javaserver.ServerSupporter')
    error('invalid supporter');
end

%% get availabilty
bAvailable = hSupporter.receiveCameraAvailable();

return;
end