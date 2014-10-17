function utrun_03()
% unit test holder for module 03_API
%
evalin('base', 'close all;clc;');

profile on;
UT_01();
profile off;
end

%% unit tests
% UT_01
function UT_01()
try
    %% start server
    stInfo = mls_api_start_server(8888);
    
    %% capture RGB picture
    stOption = struct('sFormat', '-rgb', ...
        'bShow', true);
    mls_api_get_picture(stInfo.hSupporter, stOption);
    
    %% capture grayscaled picture
    stOption.sFormat = '-gray';
    stOption.bShow = false;
    mls_api_get_picture(stInfo.hSupporter, stOption);
    
    %% stop capture
    mls_api_stop_capture(stInfo.hSupporter);
catch
end
end