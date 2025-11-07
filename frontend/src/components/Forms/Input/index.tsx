import React, { ChangeEvent } from 'react';
import { TextField, Typography } from '@mui/material';

interface InputProps {
  label: string;
  type: string;
  name: string;
  value: string;
  onChange: (event: ChangeEvent<HTMLInputElement>) => void;
  onBlur: (event: React.FocusEvent<HTMLInputElement>) => void;
  error: string | null;
}

const Input: React.FC<InputProps> = ({ label, type, name, value, onChange, error, onBlur }) => {
  return (
    <div>
      <Typography variant="body1" gutterBottom>{label}</Typography>
      <TextField
        id={name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        onBlur={onBlur}
        variant="outlined"
        fullWidth
        error={!!error}
        helperText={error}
      />
    </div>
  );
};

export default Input;
