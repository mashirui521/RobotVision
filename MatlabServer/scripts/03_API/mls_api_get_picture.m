function mPicture = mls_api_get_picture(hSupporter, stOption)
% FUNCTION aPicture = mls_api_get_picture(hSupporter, stOption)
% Get picture from client
% 
% --- INPUT
% hSupporter           JavaObject The supporter instance
% stOption             structure  option structure
%   .sFormat           char       picture format
%                                 '-rgb'      RGB
%                                 '-gray'     Gray
%   .bShow(optional)   boolean    show picture after read in
%                                 DEFAULT: true
%   .hParent(optional) handle     the parent for showing picture
%                                 if bShow is true
%                                 DEFAULT: new figure
%
% --- OUTPUT
% mPicture             matrix  The picture data matrix
% 

%% check input args
if nargin < 2
    error('empty argument');
end


if ~isequal(class(hSupporter), ...
    'com.robotvision.javaserver.ServerSupporter')
    error('invalid supporter');
end

if ~isfield(stOption, 'sFormat')
    error('sFormat is required in option.');
end

bShow = false;
if isfield(stOption, 'bShow')
    bShow = stOption.bShow;
end

hParent = '';
if bShow
    if isfield(stOption, 'hParent')
        hParent = stOption.hParent;
    end
end

%% get picture from client
fprintf('sending capture picture command...');
hSupporter.send(...
    com.robotvision.javaserver.utils.Commands.CAPTURE_PICTURE);
fprintf('...OK\n');
fprintf('getting picture...');
mPicture = mls_supporter_receivePicture(hSupporter, stOption.sFormat);
fprintf('...captured\n');

%% show picture
if bShow
    if isempty(hParent)
        figure;
        imshow(mPicture);
    else
        imshow(mPicture, 'Parent', hParent);
    end
end

return;
end