function handleSubmit(args, dialog)
{
    if (args.validationFailed)
    {
	PF(dialog).show();
    } 
    else
    {
	PF(dialog).hide();
    }
}

function start()
{
    PF('statusDialog').show();
}

function stop()
{
    PF('statusDialog').hide();
}

$(document).ready(function ()
{
    $('.ui-menuitem-link').each(function ()
    {
	if (window.location.pathname.indexOf($(this).attr('href')) !== -1)
	{
	    $(this).addClass('ui-state-hover');
	}
    });
})

//Stops a date picker opening when dialog opens
PrimeFaces.widget.Dialog.prototype.applyFocus = function ()
{
    var firstInput = this.jq.find(':not(:submit):not(:button):input:visible:enabled:first');
    if (!firstInput.hasClass('hasDatepicker'))
    {
	firstInput.focus();
    }
}