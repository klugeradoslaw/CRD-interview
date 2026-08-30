import {Controller, useFormContext} from "react-hook-form";
import {DateTimePicker} from "@mui/x-date-pickers/DateTimePicker";
import {Grid} from "@mui/material";

interface DateTimePickerControllerProps {
    name: string;
    label: string;
}

const DateTimePickerController = ({ name, label }: DateTimePickerControllerProps) => {
    const {control} = useFormContext();
    return (
        <Grid size={{ xs: 12, md: 6 }}>
            <Controller
                name={name}
                control={control}
                render={({field, fieldState: {error}}) => (
                    <DateTimePicker
                        {...field}
                        format="DD/MM/YYYY HH:mm"
                        disablePast
                        label={label}
                        slotProps={{
                            textField: {
                                fullWidth: true,
                                error: !!error,
                                helperText: error?.message,
                            },
                        }}
                    />
                )}
            />
        </Grid>
    );
};

export default DateTimePickerController;