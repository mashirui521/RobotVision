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
    stInfo = mls_api_start_server(8888);
    stOption = struct('sFormat', '-rgb', ...
        'bShow', true);
    mls_api_get_picture(stInfo.hSupporter, stOption);
    
    stOption.sFormat = '-gray';
    stOption.bShow = false;
    mls_api_get_picture(stInfo.hSupporter, stOption);
catch
end
end